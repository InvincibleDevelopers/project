package invincibleDevs.bookpago.Users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoUserDTO {
    public String id;
    public String nickname;

    @JsonProperty("connected_at")
    public String connectedAt;

    @JsonProperty("properties")  // JSON의 "properties" 필드를 매핑
    private KakaoUserProperties properties;  // KakaoUserProperties는 "properties" 필드를 위한 새로운 클래스입니다.

}
