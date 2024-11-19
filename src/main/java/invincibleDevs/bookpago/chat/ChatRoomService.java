package invincibleDevs.bookpago.chat;

import invincibleDevs.bookpago.Users.UserEntityService;
import invincibleDevs.bookpago.profile.ProfileService;
import invincibleDevs.bookpago.profile.Profile;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserEntityService userEntityService;
    private final ProfileService profileService;

    public ChatRoom saveChatRoom(Profile sender, Profile receiver, SaveMessage saveMessage) {
        if (chatRoomRepository.existsBySenderAndReceiver(sender, receiver)) {
            // 기존 ChatRoom 가져오기
            ChatRoom existingChatRoom = chatRoomRepository.findBySenderAndReceiver(sender,
                    receiver);

            // 기존 saveMessages 리스트에 새 메시지 추가
            List<SaveMessage> updatedMessages = new ArrayList<>(existingChatRoom.getSaveMessages());
            updatedMessages.add(saveMessage);

            // 새로운 ChatRoom 객체 빌드
            ChatRoom updatedChatRoom = existingChatRoom.toBuilder()
                                                       .saveMessages(
                                                               updatedMessages)  // 메시지 리스트를 업데이트
                                                       .build();
            return chatRoomRepository.save(updatedChatRoom);
        }
        List<SaveMessage> newMessages = new ArrayList<>();
        newMessages.add(saveMessage);
        ChatRoom chatRoom = ChatRoom.builder()
                                    .sender(sender)
                                    .receiver(receiver)
                                    .saveMessages(newMessages)
                                    .createDate(LocalDateTime.now())
                                    .build();
        return chatRoomRepository.save(chatRoom);
    }

//    public ChatRoom getMyChatRoom(Long senderKakaoId, Long receiverKakaoId) {
//        UserEntity me = userEntityService.getUser(senderKakaoId);
//        UserEntity partner = userEntityService.getUser(receiverKakaoId);
//        Profile myProfile = profileService.findProfilebyUser(me);
//        Profile yourProfile = profileService.findProfilebyUser(partner);
//
//        ChatRoom thisChatRoom;
//
//        if ((!chatRoomRepository.existsByMeAndPartner(myProfile, yourProfile))
//                && (!chatRoomRepository.existsByMeAndPartner(yourProfile, myProfile)) && (me.getId()
//                != yourProfile.getUserEntity().getKakaoId())) {
//            thisChatRoom = ChatRoom.builder()
//                                   .me(myProfile)
//                                   .partner(yourProfile)
//                                   .build();
//            chatRoomRepository.save(thisChatRoom);
//            return thisChatRoom;
//        } else if (chatRoomRepository.existsByMeAndPartner(myProfile,
//                yourProfile)) { //나한테연결된 디엠페이지 잇을때. 삭제할때도 쓰임.
//            thisChatRoom = chatRoomRepository.findByMeAndPartner(me.getId(),
//                    partner.getId());// 이거 삭제됐을때 -> temp에서찾기.
//            return thisChatRoom;
//        } else { //상대방한ㄴ테연결된 디엠페이지잇을때
//            thisChatRoom = chatRoomRepository.findByMeAndPartner(partner.getId(), me.getId());
//            return thisChatRoom;
//        }
//    }
}
