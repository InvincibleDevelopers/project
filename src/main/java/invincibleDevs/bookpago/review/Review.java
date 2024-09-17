package invincibleDevs.bookpago.review;

import invincibleDevs.bookpago.profile.model.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)  // toBuilder = true 설정
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 포함한 생성자 추가
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 이거를 username으로 설정하는것도 고려해보기
    private Long id;

    private int rating;

    private String content;

    private String isbnCode; //도서고유번호

    @ManyToOne
    @JoinColumn(name = "author_profile_id")
    private Profile profile;
}
