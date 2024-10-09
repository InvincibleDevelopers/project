package invincibleDevs.bookpago.chat;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder(toBuilder = true)  // toBuilder = true 설정
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 포함한 생성자 추가
public class MessageDto {

    private Long id;
    private Long senderId; // 보낸 사람
    private Long receiverId; // 받는 사람
    private String content; // 메시지
    private LocalDateTime createdAt;
}
