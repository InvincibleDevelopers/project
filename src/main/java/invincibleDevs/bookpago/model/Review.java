package invincibleDevs.bookpago.model;

import invincibleDevs.bookpago.profile.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String content;

    @ManyToOne
    private Profile author;
}
