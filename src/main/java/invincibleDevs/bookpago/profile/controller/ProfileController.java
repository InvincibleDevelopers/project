package invincibleDevs.bookpago.profile.controller;

import invincibleDevs.bookpago.profile.facade.ProfileFacade;
import invincibleDevs.bookpago.profile.request.FollowRequest;
import invincibleDevs.bookpago.profile.request.ProfileRequest;
import invincibleDevs.bookpago.profile.request.UpdateProfileRequest;
import invincibleDevs.bookpago.profile.response.FollowingListDto;
import invincibleDevs.bookpago.profile.response.ProfileResponse;
import org.springframework.data.domain.Page;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileFacade profileFacade;

//    @GetMapping("/page")
//    public ResponseEntity<ProfileResponse> profilePage(
//            @ApiParam(value = "토큰", required = true)
//            @RequestHeader(value = "Authorization", required = true) String serverToken,
//            @RequestParam(value = "username") String username
//    ) {
//
//        ProfileRequest profileRequest = new ProfileRequest(serverToken);
//        return ResponseEntity.ok(profileFacade.getProfile(profileRequest,username));
//    }

    @PostMapping("/updateimage")
    public ResponseEntity<ProfileResponse> updateProfileImage(
            @ApiParam(value = "변경할 이미지 파일", required = true)
//            @RequestHeader(value = "Authorization", required = true) String serverToken,
            @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.ok(profileFacade.updateProfileImage(file));
    }

    @PostMapping("/updatenickname")
    public ResponseEntity<ProfileResponse> updateNickname(
            @ApiParam(value = "변경할 닉네임", required = true)
//            @RequestHeader(value = "Authorization", required = true) String serverToken,
            @RequestBody UpdateProfileRequest updateProfileRequest
    ) {
        System.out.println("================================");
        return ResponseEntity.ok(profileFacade.updateNickname(updateProfileRequest));
    }

    @PostMapping("/updateintroduce")
    public ResponseEntity<ProfileResponse> updateIntroduce(
            @ApiParam(value = "변경할 소개글", required = true)
//            @RequestHeader(value = "Authorization", required = true) String serverToken,
            @RequestBody UpdateProfileRequest updateProfileRequest
    ) {
        return ResponseEntity.ok(profileFacade.updateIntroduce(updateProfileRequest));
    }

    @PostMapping("/updatefollowing")
    public ResponseEntity<String> updatefollowing(
            @ApiParam(value ="follower, followee 필수", required = true)
            @RequestBody FollowRequest followRequest)  {

        if(profileFacade.updateFollow(followRequest)) {
            return ResponseEntity.ok("Success Follow.");
        } else {
            return ResponseEntity.ok("Success Unfollow.");
        }
    }

    @GetMapping("/getfollowers")
    public ResponseEntity<Page<FollowingListDto>> getfollowers(
//            @RequestHeader(value = "Authorization", required = true) String serverToken,
            @RequestHeader(value = "username", required = true) String username, int page
    ) {
        return ResponseEntity.ok(profileFacade.getFollowers(username,page));
    }



}
