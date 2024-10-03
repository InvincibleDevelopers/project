package invincibleDevs.bookpago.chat;

import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatRoomService chatRoomService;
    private final ProfileService profileService;
    private final SaveMessageRepository saveMessageRepository;

    @MessageMapping("/hello") //이렇게 경로를 지정해주어야한다.이게 일종의 방 이름이다
    @SendTo("/sub/message")
    public MessageDto messaging(SendMessage sendMessage) {
//        Profile sender = profileService.findProfilebyUsername(sendMessage.getSender());
        logger.info("messaging...");
        //아직 룸에 대해서는 제대로 처리가 되지 않은거 같다.
        //ChatRoom chatRoom = chatRoomService.getMyChatRoom(sendMessage.getSender(), sendMessage.getReceiver());
        Profile sender = profileService.findByKakaoId(sendMessage.senderId());
        Profile receiver = profileService.findByKakaoId(sendMessage.receiverId());

        SaveMessage saveMessage = SaveMessage.builder()
                                             .sender(sendMessage.senderId())
                                             .receiver(sendMessage.receiverId())
                                             .content(sendMessage.content())
                                             .createDate(sendMessage.getCreatedAtAsLocalDateTime())
                                             .build();
        saveMessageRepository.save(saveMessage);
        chatRoomService.saveChatRoom(sender, receiver, saveMessage);

        MessageDto messageDto = MessageDto.builder()
                                          .id(saveMessage.getId())
                                          .content(saveMessage.getContent())
                                          .createdAt(saveMessage.getCreateDate())
                                          .senderId(saveMessage.getSender())
                                          .receiverId(saveMessage.getReceiver())
                                          .build();
        return messageDto;

    }
}
