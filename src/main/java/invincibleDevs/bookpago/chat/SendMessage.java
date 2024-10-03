package invincibleDevs.bookpago.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public record SendMessage(
        Long senderId,
        Long receiverId,
        String content,
        String createdAt,
        Optional<MultipartFile> file
) {

    // String을 LocalDateTime으로 변환하는 메서드
    public LocalDateTime getCreatedAtAsLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss"); // 클라이언트가 보내는 날짜 형식
        return LocalDateTime.parse(createdAt, formatter);
    }
}
