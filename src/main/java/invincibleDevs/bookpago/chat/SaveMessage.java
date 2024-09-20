package invincibleDevs.bookpago.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)  // toBuilder = true 설정
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 포함한 생성자 추가
public class SaveMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 이거를 username으로 설정하는것도 고려해보기
    private Long id;
    private String content;
    private String sender;
    private String receiver;
    private LocalDateTime createDate;

    @ManyToOne
    private ChatRoom chatRoom;


}

