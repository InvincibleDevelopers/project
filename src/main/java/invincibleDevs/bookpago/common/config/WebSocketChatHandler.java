package invincibleDevs.bookpago.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import invincibleDevs.bookpago.chat.MessageDto;
import invincibleDevs.bookpago.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends AbstractWebSocketHandler {
    private final ObjectMapper mapper;
    //현재 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    //chatRoomId : {session1,session2}
    private final Map<Long,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    //소켓 연결 확인
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        log.info("{} connected.", session.getId());
        sessions.add(session);
    }

    //소켓 통신 시 메세지의 전송을 다루는 부분
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        String payload = message.getPayload();
        log.info("payload {}", payload);

        //페이로드 -> MessageDto로 변환
        MessageDto messageDto = mapper.readValue(payload, MessageDto.class);
        log.info("session {}", messageDto.toString());

        Long chatRoomId = messageDto.getChatRoomId();

        // 메모리 상에 채팅방에 대한 세션 없으면 만들어줌
        chatRoomSessionMap.computeIfAbsent(chatRoomId, k -> new HashSet<>());
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

        // message 에 담긴 타입을 확인한다.
        // 이때 message 에서 getType 으로 가져온 내용이
        // ChatDTO 의 열거형인 MessageType 안에 있는 ENTER 과 동일한 값이라면
        if (messageDto.getMessageType().equals(MessageDto.MessageType.ENTER)){
            chatRoomSession.add(session);
        }
        if (chatRoomSession.size() >= 3){
            removeClosedSession(chatRoomSession);
        }
        sendMessageToChatRoom(messageDto, chatRoomSession);

    }

    //소켓 종료 확인
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} closed Connection.",session.getId());
        sessions.remove(session);
    }

    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(sess -> sessions.contains(sess));
    }

    private void sendMessageToChatRoom(MessageDto messageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream().forEach(sess -> sendMessage(sess,messageDto));
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        }catch (IOException e){
            log.error(e.getMessage(),e);
        }
    }

}
