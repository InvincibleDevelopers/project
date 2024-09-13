package invincibleDevs.bookpago.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;

    private String author;

//    private String genre;
//
//    private String summary;
//
//    private String bookImage; //url? 파일?
//
//    private Long average_rating;


}
