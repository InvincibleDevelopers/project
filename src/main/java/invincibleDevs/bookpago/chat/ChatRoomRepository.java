package invincibleDevs.bookpago.chat;

import invincibleDevs.bookpago.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    boolean existsByMeAndPartner(Profile me, Profile partner);

    @Query("select cr from ChatRoom cr where cr.me.id = :myId and cr.partner.id = :partnerId")
    ChatRoom findByMeAndPartner(@Param("myId")Long myId, @Param("partnerId")Long partnerId);

    @Query("select cr from ChatRoom cr where cr.me.id = :myId and cr.partner.id = :myId")
    List<ChatRoom> findAllMyChatRoomList(@Param("myId")Long myId);
}