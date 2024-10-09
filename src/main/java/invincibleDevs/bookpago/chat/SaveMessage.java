package invincibleDevs.bookpago.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Long sender;
    private Long receiver;
    private LocalDateTime createDate;

    @ManyToOne
    private ChatRoom chatRoom;


}

