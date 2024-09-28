package invincibleDevs.bookpago.chat;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatRoomService chatRoomService;
    private final SaveMessageRepository saveMessageRepository;

    @MessageMapping("/hello") //이렇게 경로를 지정해주어야한다.이게 일종의 방 이름이다
    @SendTo("/sub/message")
    public MessageDto messaging(SendMessage sendMessage) {
//        Profile sender = profileService.findProfilebyUsername(sendMessage.getSender());
        logger.info("messaging...");
        //아직 룸에 대해서는 제대로 처리가 되지 않은거 같다.
        //ChatRoom chatRoom = chatRoomService.getMyChatRoom(sendMessage.getSender(), sendMessage.getReceiver());
        SaveMessage saveMessage = SaveMessage.builder()
                                             .content(sendMessage.getContent())
                                             .sender(sendMessage.getSender())
                                             .receiver(sendMessage.getReceiver())
                                             .build();
        saveMessageRepository.save(saveMessage);

        MessageDto messageDto = MessageDto.builder()
                                          .id(saveMessage.getId())
                                          .message(saveMessage.getContent())
                                          .createdAt(saveMessage.getCreateDate())
                                          .senderUsername(saveMessage.getSender())
                                          .senderUsername(saveMessage.getReceiver())
                                          .build();
        return messageDto;

    }
}
