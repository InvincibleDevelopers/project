package invincibleDevs.bookpago.book;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookService {
    @Value("${api.naver.id}")
    private String clientId;

    @Value("${api.naver.password}")
    private String clientPassword;

    private final BookRepository bookRepository;

    public BookDetailDTO getBookInfo(Long bookIsbn) {
        try {
            JsonNode rootNode = bookJson(bookIsbn.toString(), 1, 10);
            JsonNode itemsNode = rootNode.get("items");

            if (itemsNode.isEmpty()) {
                throw new NoSuchElementException("No book found with the provided ISBN in the API response.");
            }

            JsonNode bookNode = itemsNode.get(0);

            Book book = bookRepository.findById(bookIsbn)
                    .orElse(new Book(bookIsbn, (float)0.0));

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
        } catch(NoSuchElementException e) {
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

    public JsonNode bookJson(String query, int page, int size) throws Exception {
        int bookStart = (page - 1) * size + 1;
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("v1/search/book.json")
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

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readTree(responseBody);
    }
}
