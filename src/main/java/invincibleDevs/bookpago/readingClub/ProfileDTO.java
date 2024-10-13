package invincibleDevs.bookpago.readingClub;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileDTO {
    private Long kakaoId;
    private String nickname;
    private String imgUrl;
}
