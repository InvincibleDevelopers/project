package invincibleDevs.bookpago.chat;

import jakarta.websocket.server.ServerEndpoint;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Builder(toBuilder = true)  // toBuilder = true 설정
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 포함한 생성자 추가
public class MessageDto {
    private Long id;
    private String senderUsername; // 보낸 사람
    private String receiverUsername; // 받는 사람
    private String message; // 메시지
    private LocalDateTime createdAt;
}
