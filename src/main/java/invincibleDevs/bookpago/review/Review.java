package invincibleDevs.bookpago.review;

import invincibleDevs.bookpago.profile.Profile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 포함한 생성자 추가
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String content;

    @ManyToOne
    private Profile author;
}
