package invincibleDevs.bookpago.chat;

import invincibleDevs.bookpago.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    boolean existsBySenderAndReceiver(Profile sender, Profile receiver);

    ChatRoom findBySenderAndReceiver(Profile sender, Profile receiver);

//    List<ChatRoom> findAllMyChatRoomList(Profile sender);
}