package invincibleDevs.bookpago.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SendMessage {
    private String content;
    private String sender;
    private String receiver;

//    @JsonProperty("dmImage")
//    private DmImageDto dmImageDto;
}
