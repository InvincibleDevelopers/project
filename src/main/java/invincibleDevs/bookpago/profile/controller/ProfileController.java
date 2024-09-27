package invincibleDevs.bookpago.profile.controller;

import invincibleDevs.bookpago.book.BookDTO;
import invincibleDevs.bookpago.profile.facade.ProfileFacade;
import invincibleDevs.bookpago.profile.request.FollowRequest;
import invincibleDevs.bookpago.profile.request.ProfileRequest;
import invincibleDevs.bookpago.profile.request.UpdateProfileRequest;
import invincibleDevs.bookpago.profile.response.FollowingListDto;
import invincibleDevs.bookpago.profile.response.ProfileResponse;
import invincibleDevs.bookpago.review.MyReviewDto;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileFacade profileFacade;

    @GetMapping("/page")
    public ResponseEntity<ProfileResponse> profilePage(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "page", defaultValue = "0") int page,  // 쿼리 파라미터로 페이지 번호를 받음
            @RequestParam(value = "size", defaultValue = "5") int size) { // 쿼리 파라미터로 페이지 크기를 받음

        ProfileRequest profileRequest = new ProfileRequest(username);
        return ResponseEntity.ok(profileFacade.getProfile(profileRequest, username, page, size));
    }

    @PostMapping("/image")
    public ResponseEntity<ProfileResponse> updateProfileImage(
            @ModelAttribute UpdateProfileRequest updateProfileRequest) {
        System.out.println(updateProfileRequest.username());
        System.out.println(updateProfileRequest);
        MultipartFile file = updateProfileRequest.file().orElseThrow();
        return ResponseEntity.ok(profileFacade.updateProfileImage(file, updateProfileRequest));
    }

    @PostMapping("/nickname")
    public ResponseEntity<ProfileResponse> updateNickname(
            @ApiParam(value = "변경할 닉네임", required = true)
            @RequestBody UpdateProfileRequest updateProfileRequest) {
        return ResponseEntity.ok(profileFacade.updateNickname(updateProfileRequest));
    }

    @PostMapping("/introduce")
    public ResponseEntity<ProfileResponse> updateIntroduce(
            @ApiParam(value = "변경할 소개글", required = true)
            @RequestBody UpdateProfileRequest updateProfileRequest) {
        return ResponseEntity.ok(profileFacade.updateIntroduce(updateProfileRequest));
    }

    @PostMapping("/follow")
    public ResponseEntity<String> updatefollowing(
            @ApiParam(value = "follower, followee 필수", required = true)
            @RequestBody FollowRequest followRequest) {

        if (profileFacade.updateFollow(followRequest)) {
            return ResponseEntity.ok("Success Follow.");
        } else {
            return ResponseEntity.ok("Success Unfollow.");
        }
    }

    @GetMapping("/follower")
    public ResponseEntity<Page<FollowingListDto>> getfollowers( //내가 상대의 팔로워 리스트 보는 요청
            @RequestParam(value = "targetId") Long targetId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) {

        System.out.println(targetId);
        return ResponseEntity.ok(profileFacade.getFollowers(targetId, 0, 10));
    }

    @GetMapping("/followee")
    public ResponseEntity<Page<FollowingListDto>> getfollowees( //내가 상대의 팔로잉 리스트 보는 요청
            @RequestParam(value = "targetId") Long targetId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(profileFacade.getFollowees(targetId, page, size));
    }

    @GetMapping("/library")
    public ResponseEntity<List<MyReviewDto>> getMyBooks( //나의 서재
            @RequestParam(value = "nickname") String nickname,
            @RequestParam(value = "lastBookId", required = false) Long lastBookIsbn,
            @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(profileFacade.getMyBooks(nickname, lastBookIsbn, size));
    }

    @GetMapping("/wishBook")
    public ResponseEntity<List<BookDTO>> getWishBooks(
            @RequestParam(value = "nickname") String nickname,
            @RequestParam(value = "lastBookId", required = false) Long lastBookId,
            @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(profileFacade.getMyWishBooks(nickname, lastBookId, size));
    }


}
