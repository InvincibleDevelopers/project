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

    @GetMapping("/page")
    public ResponseEntity<ProfileResponse> profilePage(
            @ApiParam(value = "토큰", required = true)
            @RequestHeader(value = "Authorization", required = true) String serverToken,
            @RequestParam(value = "username") String username,
            @RequestParam(defaultValue = "0") int page,  // 쿼리 파라미터로 페이지 번호를 받음
            @RequestParam(defaultValue = "5") int size) { // 쿼리 파라미터로 페이지 크기를 받음


        ProfileRequest profileRequest = new ProfileRequest(username);
        return ResponseEntity.ok(profileFacade.getProfile(profileRequest,username,page,size));
    }

    @PostMapping("/updateimage")
    public ResponseEntity<ProfileResponse> updateProfileImage(
            @ApiParam(value = "변경할 이미지 파일", required = true)
            @RequestHeader(value = "Authorization", required = true) String serverToken,
            @RequestParam(value = "username") String username,
            @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.ok(profileFacade.updateProfileImage(file));
    }

    @PostMapping("/updatenickname")
    public ResponseEntity<ProfileResponse> updateNickname(
            @ApiParam(value = "변경할 닉네임", required = true)
            @RequestHeader(value = "Authorization", required = true) String serverToken,
            @RequestParam(value = "username") String username,
            @RequestBody UpdateProfileRequest updateProfileRequest
    ) {
        return ResponseEntity.ok(profileFacade.updateNickname(updateProfileRequest));
    }

    @PostMapping("/updateintroduce")
    public ResponseEntity<ProfileResponse> updateIntroduce(
            @ApiParam(value = "변경할 소개글", required = true)
            @RequestHeader(value = "Authorization", required = true) String serverToken,
            @RequestParam(value = "username") String username,
            @RequestBody UpdateProfileRequest updateProfileRequest
    ) {
        return ResponseEntity.ok(profileFacade.updateIntroduce(updateProfileRequest));
    }

    @PostMapping("/updatefollowing")
    public ResponseEntity<String> updatefollowing( // 로직추가 : 팔로워가 접속 user인지!
            @ApiParam(value ="follower, followee 필수", required = true)
            @RequestHeader(value = "Authorization", required = true) String serverToken,
            @RequestParam(value = "username") String username,
            @RequestBody FollowRequest followRequest)  {

        if(profileFacade.updateFollow(followRequest)) {
            return ResponseEntity.ok("Success Follow.");
        } else {
            return ResponseEntity.ok("Success Unfollow.");
        }
    }

    @GetMapping("/getfollowerlist")
    public ResponseEntity<Page<FollowingListDto>> getfollowers( //내가 상대의 팔로워 리스트 보는 요청
            @RequestHeader(value = "Authorization", required = true) String serverToken,
            @RequestParam(value = "targetId") Long targetId,
            @RequestParam(defaultValue = "0") int page,  // 쿼리 파라미터로 페이지 번호를 받음
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(profileFacade.getFollowers(targetId,page,size));
    }
    @GetMapping("/getfolloweelist")
    public ResponseEntity<Page<FollowingListDto>> getfollowees( //내가 상대의 팔로잉 리스트 보는 요청
            @RequestHeader(value = "Authorization", required = true) String serverToken,
            @RequestParam(value = "targetId") Long targetId,
            @RequestParam(defaultValue = "0") int page,  // 쿼리 파라미터로 페이지 번호를 받음
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(profileFacade.getFollowees(targetId,page,size));
    }



}
