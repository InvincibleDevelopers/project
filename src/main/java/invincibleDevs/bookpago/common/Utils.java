package invincibleDevs.bookpago.common;
import invincibleDevs.bookpago.common.exception.CustomException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {
    // 공통으로 사용할 메서드를 static으로 정의합니다.
    public static String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // 인증된 사용자 이름 반환
        }
        // 인증되지 않은 경우 예외를 던짐
        throw new CustomException("User is not authenticated.");
    }
}
