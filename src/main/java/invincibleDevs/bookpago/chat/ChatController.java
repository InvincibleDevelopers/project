package invincibleDevs.bookpago.chat;

import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.Users.service.UserEntityService;
import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final SaveMessageRepository saveMessageRepository;

    @MessageMapping
    @SendTo("/sub/message")
    public MessageDto messaging(SendMessage sendMessage) {
//        Profile sender = profileService.findProfilebyUsername(sendMessage.getSender());

        ChatRoom chatRoom = chatRoomService.getMyChatRoom(sendMessage.getSender(), sendMessage.getReceiver());
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
