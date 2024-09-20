package invincibleDevs.bookpago.chat;

import invincibleDevs.bookpago.profile.model.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)  // toBuilder = true 설정
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 포함한 생성자 추가
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Profile me;

    @ManyToOne
    private Profile partner;// 여기서 receivedmessage불러와서 출력하면 되나??

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE ,fetch = FetchType.EAGER) //many라고 해봤자 나랑 상대방 각각의 dmpage에저장?dkslsrk nn아닌가 ㅜㅜ
    List<SaveMessage> saveMessages;

    private LocalDateTime createDate;


}
