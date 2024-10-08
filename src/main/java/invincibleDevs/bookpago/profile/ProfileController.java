package invincibleDevs.bookpago.profile;

import invincibleDevs.bookpago.book.BookDTO;
import invincibleDevs.bookpago.profile.request.FollowRequest;
import invincibleDevs.bookpago.profile.request.ProfileRequest;
import invincibleDevs.bookpago.profile.request.UpdateProfileRequest;
import invincibleDevs.bookpago.profile.response.FollowingListDto;
import invincibleDevs.bookpago.review.MyReviewDto;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{kakaoId}")
    public ResponseEntity<?> profilePage(
            @PathVariable(value = "kakaoId") Long kakaoId,
            @RequestParam(value = "currentUserKakaoId") Long currentUserKakaoId) { // 쿼리 파라미터로 페이지 크기를 받음

        ProfileRequest profileRequest = new ProfileRequest(kakaoId);
        return ResponseEntity.ok(
                profileFacade.getProfile(profileRequest, currentUserKakaoId, 0, 10));
    }

    @PatchMapping("/image")
    public ResponseEntity<?> updateProfileImage(
            @ModelAttribute UpdateProfileRequest updateProfileRequest) {
        MultipartFile file = updateProfileRequest.file().orElseThrow();
        return ResponseEntity.ok(profileFacade.updateProfileImage(file, updateProfileRequest));
    }

    @PatchMapping("/{kakaoId}")
    public ResponseEntity<?> updateNickname(
            @ApiParam(value = "변경할 닉네임, 소개글", required = true)
            @RequestBody UpdateProfileRequest updateProfileRequest) {
        return profileFacade.updateNicknameAndIntroduction(updateProfileRequest);
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

    @GetMapping("/{kakaoId}/follower")
    public ResponseEntity<Page<FollowingListDto>> getfollowers( //내가 상대의 팔로워 리스트 보는 요청
            @PathVariable(value = "kakaoId") Long kakaoId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(profileFacade.getFollowers(kakaoId, page, size));
    }

    @GetMapping("/{kakaoId}/following")
    public ResponseEntity<Page<FollowingListDto>> getfollowees( //내가 상대의 팔로잉 리스트 보는 요청
            @PathVariable(value = "kakaoId") Long kakaoId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(profileFacade.getFollowees(kakaoId, page, size));
    }

    @GetMapping("/library")
    public ResponseEntity<List<MyReviewDto>> getMyBooks( //나의 서재
            @RequestParam(value = "kakaoId") Long kakaoId,
            @RequestParam(value = "lastBookId", required = false) Long lastBookIsbn,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(profileFacade.getMyBooks(kakaoId, lastBookIsbn, size));
    }

    @GetMapping("/wishbook")
    public ResponseEntity<List<BookDTO>> getWishBooks(
            @RequestParam(value = "kakaoId") Long kakaoId,
            @RequestParam(value = "lastBookId", required = false) Long lastBookId,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(profileFacade.getMyWishBooks(kakaoId, lastBookId, size));
    }


}
