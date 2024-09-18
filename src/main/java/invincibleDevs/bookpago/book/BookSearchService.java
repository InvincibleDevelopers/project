package invincibleDevs.bookpago.book;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookSearchService {
    @Value("${api.naver.id}")
    private String clientId;

    @Value("${api.naver.password}")
    private String clientPassword;

    public List<BookDTO> searchBooks(String query, int page, int size) throws Exception {
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
        JsonNode itemsNode = objectMapper.readTree(responseBody).get("items");

        List<BookDTO> books = new ArrayList<>();
        for (JsonNode itemNode : itemsNode) {
            Long isbn = itemNode.get("isbn").asLong();
            String title = itemNode.get("title").asText();
            String author = itemNode.get("author").asText();
            String image = itemNode.get("image").asText();
            String description = itemNode.get("description").asText();
            String publisher = itemNode.get("publisher").asText();
            String pubdate = itemNode.get("pubdate").asText();

            BookDTO book = new BookDTO(isbn, title, author, image, description, publisher, pubdate);
            books.add(book);
        }

        return books;
    }
}
