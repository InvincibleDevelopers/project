package invincibleDevs.bookpago.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaveMessageRepository extends JpaRepository<SaveMessage, Long> {

    @Query("SELECT s FROM SaveMessage s WHERE s.chatRoom.id = :chatRoomId AND s.sender = :sender")
    List<SaveMessage> findAllBySenderAndChatRoomId(@Param("sender") String sender, @Param("chatRoomId") Long chatRoomId);

    @Query("SELECT s FROM SaveMessage s WHERE s.chatRoom.id = :chatRoomId ORDER BY s.createDate ASC")
    List<SaveMessage> findAllByChatRoomIdOrderByCreateDateAsc(@Param("chatRoomId") Long chatRoomId);

}





