package invincibleDevs.bookpago.chat;

import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.Users.service.UserEntityService;
import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserEntityService userEntityService;
    private final ProfileService profileService;

    public ChatRoom getMyChatRoom(Long senderKakaoId, Long receiverKakaoId) {
        UserEntity me = userEntityService.getUser(senderKakaoId);
        UserEntity partner = userEntityService.getUser(receiverKakaoId);
        Profile myProfile = profileService.findProfilebyUser(me);
        Profile yourProfile = profileService.findProfilebyUser(partner);

        ChatRoom thisChatRoom;

        if ((!chatRoomRepository.existsByMeAndPartner(myProfile, yourProfile))
                && (!chatRoomRepository.existsByMeAndPartner(yourProfile, myProfile)) && (me.getId()
                != yourProfile.getUserEntity().getKakaoId())) {
            thisChatRoom = ChatRoom.builder()
                                   .me(myProfile)
                                   .partner(yourProfile)
                                   .build();
            chatRoomRepository.save(thisChatRoom);
            return thisChatRoom;
        } else if (chatRoomRepository.existsByMeAndPartner(myProfile,
                yourProfile)) { //나한테연결된 디엠페이지 잇을때. 삭제할때도 쓰임.
            thisChatRoom = chatRoomRepository.findByMeAndPartner(me.getId(),
                    partner.getId());// 이거 삭제됐을때 -> temp에서찾기.
            return thisChatRoom;
        } else { //상대방한ㄴ테연결된 디엠페이지잇을때
            thisChatRoom = chatRoomRepository.findByMeAndPartner(partner.getId(), me.getId());
            return thisChatRoom;
        }
    }
}
