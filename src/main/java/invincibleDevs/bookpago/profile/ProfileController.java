package invincibleDevs.bookpago.profile;

import invincibleDevs.bookpago.Users.dto.request.KakaoJoinRequest;
import invincibleDevs.bookpago.Users.dto.request.SignInRequest;
import invincibleDevs.bookpago.Users.dto.response.SignInResponse;
import invincibleDevs.bookpago.Users.dto.response.SignUpResponse;
import invincibleDevs.bookpago.profile.request.ProfileRequest;
import invincibleDevs.bookpago.profile.request.UpdateProfileRequest;
import invincibleDevs.bookpago.profile.response.ProfileResponse;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileFacade profileFacade;

    @GetMapping("/page")
    public ResponseEntity<ProfileResponse> myProfile(
            @ApiParam(value = "토큰", required = true)
            @RequestHeader(value = "Authorization", required = true) String serverToken
    ) {

        ProfileRequest profileRequest = new ProfileRequest(serverToken);
        return ResponseEntity.ok(profileFacade.getProfile(profileRequest));
    }

    @PostMapping("/update-image")
    public ResponseEntity<ProfileResponse> updateProfileImage(
            @ApiParam(value = "변경할 이미지 파일", required = true)
            @RequestHeader(value = "Authorization", required = true) String serverToken,
            @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.ok(profileFacade.updateProfileImage(file));
    }

    @PostMapping("/update-nickname")
    public ResponseEntity<ProfileResponse> updateNickname(
            @ApiParam(value = "변경할 닉네임", required = true)
            @RequestHeader(value = "Authorization", required = true) String serverToken,
            @RequestBody UpdateProfileRequest updateProfileRequest
    ) {
        return ResponseEntity.ok(profileFacade.updateNickname(updateProfileRequest));
    }

    @PostMapping("/update-introduce")
    public ResponseEntity<ProfileResponse> updateIntroduce(
            @ApiParam(value = "변경할 소개글", required = true)
            @RequestHeader(value = "Authorization", required = true) String serverToken,
            @RequestBody UpdateProfileRequest updateProfileRequest
    ) {
        return ResponseEntity.ok(profileFacade.updateIntroduce(updateProfileRequest));
    }


}
