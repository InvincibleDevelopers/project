package invincibleDevs.bookpago.profile.controller;

import invincibleDevs.bookpago.profile.facade.ProfileFacade;
import invincibleDevs.bookpago.profile.request.FollowRequest;
import invincibleDevs.bookpago.profile.request.ProfileRequest;
import invincibleDevs.bookpago.profile.request.UpdateProfileRequest;
import invincibleDevs.bookpago.profile.response.ProfileResponse;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;

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
    public ResponseEntity<Boolean> updatefollowing(
            @ApiParam(value ="follower, followee 필수", required = true)
            @RequestBody FollowRequest followRequest)  {

        return ResponseEntity.ok(profileFacade.updateFollow(followRequest));
    }

//    @PostMapping("/unfollow")
//    public String unfollow(Model model, Principal principal, @RequestParam(value = "profileId") Long profileId) throws UnsupportedEncodingException {
////        플필아디 받고
////                팔로잉맵에 추가 현재로그인 foller로 플필아디followee로
//        Profile followee = profileService.getProfileById(profileId);
//        Member sitemember = this.memberService.getMember(principal.getName());
//        Profile follower = sitemember.getProfile();
//        followingMapService.deletefollowingMap(follower, followee);
//
//        String encodedProfileName = URLEncoder.encode(followee.getProfileName(), "UTF-8");
//        return "redirect:/profile/detail/" + encodedProfileName;
//    }


}
