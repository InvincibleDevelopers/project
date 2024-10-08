package invincibleDevs.bookpago.book;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class BookService {

    @Value("${api.naver.id}")
    private String clientId;

    @Value("${api.naver.password}")
    private String clientPassword;

    @Value("${api.aladin.key}")
    private String aladinApiKey;

    private final String NAVER_API_URL = "https://openapi.naver.com/v1/search/book.json";
    private final String ALADIN_API_URL = "http://www.aladin.co.kr/ttb/api/ItemList.aspx";

    private final BookRepository bookRepository;

    public BookDetailDTO getBookInfo(Long bookIsbn) {
        try {
            JsonNode rootNode = bookJson(bookIsbn.toString(), 1, 10);
            JsonNode itemsNode = rootNode.get("items");

            if (itemsNode.isEmpty()) {
                throw new NoSuchElementException(
                        "No book found with the provided ISBN in the API response.");
            }

            JsonNode bookNode = itemsNode.get(0);

            Book book = bookRepository.findById(bookIsbn)
                                      .orElse(new Book(bookIsbn, (float) 0.0));

            BookDetailDTO bookDetailDTO = new BookDetailDTO();
            bookDetailDTO.setIsbn(book.getIsbn());
            bookDetailDTO.setAverage_rating(book.getAverage_rating());
            bookDetailDTO.setTitle(bookNode.get("title").asText());
            bookDetailDTO.setAuthor(bookNode.get("author").asText());
            bookDetailDTO.setImage(bookNode.get("image").asText());
            bookDetailDTO.setDescription(bookNode.get("description").asText());
            bookDetailDTO.setPubdate(bookNode.get("pubdate").asText());
            bookDetailDTO.setPublisher(bookNode.get("publisher").asText());

            return bookDetailDTO;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Book not found", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BookSearchDTO searchBooks(String query, int page, int size) throws Exception {
        JsonNode rootNode = bookJson(query, page, size);
        JsonNode itemsNode = rootNode.get("items");
        int total = rootNode.get("total").asInt();

        List<BookDTO> books = new ArrayList<>();
        for (JsonNode itemNode : itemsNode) {
            Long isbn = itemNode.get("isbn").asLong();
            String title = itemNode.get("title").asText();
            String author = itemNode.get("author").asText();
            String image = itemNode.get("image").asText();

            BookDTO book = new BookDTO(isbn, title, author, image);
            books.add(book);
        }
        return new BookSearchDTO(total, books);
    }

    public BookDTO searchBook(String query) throws Exception {
        JsonNode rootNode = bookJson(query, 1, 1);
        JsonNode itemsNode = rootNode.get("items");

        BookDTO book = null;
        for (JsonNode itemNode : itemsNode) {
            Long isbn = itemNode.get("isbn").asLong();
            String title = itemNode.get("title").asText();
            String author = itemNode.get("author").asText();
            String image = itemNode.get("image").asText();

            book = new BookDTO(isbn, title, author, image);
        }
        return book;
    }

    public BookSearchDTO getBestsellers() throws Exception {
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(ALADIN_API_URL)
                    .queryParam("ttbkey", aladinApiKey)
                    .queryParam("QueryType", "Bestseller")
                    .queryParam("MaxResults", 10)
                    .queryParam("start", 1)
                    .queryParam("SearchTarget", "Book")
                    .queryParam("CategoryId", 0)
                    .queryParam("output", "js")
                    .queryParam("Version", "20131101")
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            String responseBody = restTemplate.getForObject(uri, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode itemsNode = rootNode.get("item");

            List<BookDTO> books = new ArrayList<>();
            for (JsonNode itemNode : itemsNode) {
                Long isbn = Long.parseLong(itemNode.get("isbn13").asText());
                String title = itemNode.get("title").asText();
                String author = itemNode.get("author").asText();
                String image = itemNode.get("cover").asText();

                BookDTO book = new BookDTO(isbn, title, author, image);
                books.add(book);
            }

            int total = books.size();

            return new BookSearchDTO(total, books);
        } catch (Exception e) {
            throw new Exception(
                    "Error occurred while fetching bestsellers from Aladin API: " + e.getMessage());
        }
    }

    public JsonNode bookJson(String query, int page, int size) throws Exception {
        int bookStart = (page - 1) * size + 1;
        URI uri = UriComponentsBuilder
                .fromUriString(NAVER_API_URL)
                .queryParam("query", query)
                .queryParam("display", size)
                .queryParam("start", bookStart)
                .queryParam("sort", "sim")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientPassword);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity,
                String.class);
        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readTree(responseBody);
    }

    public List<String> extractRecommendations(String response) {
        List<String> recommendList = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+\\.\\s([^\\(]+\\([^\\)]+\\))");
        Matcher matcher = pattern.matcher(response);
        while (matcher.find()) {
            String titleAndAuthor = matcher.group(1).trim();
            recommendList.add(titleAndAuthor);
        }
        return recommendList;
    }

}
