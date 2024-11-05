package invincibleDevs.bookpago.review;

import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewFacade reviewFacade;

    @GetMapping("/{bookIsbn:\\d+}")
    public ResponseEntity<?> getReviews(
            @PathVariable("bookIsbn") Long bookIsbn,
            @RequestParam(name = "kakaoId") Long kakaoId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        try {
            return ResponseEntity.ok(reviewFacade.getReviews(kakaoId, bookIsbn, page, size));
        } catch (Exception e) {
            // 예외가 발생한 경우 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/likes")
    public ResponseEntity<?> addLikes(
            @ApiParam(value = "리뷰 좋아요", required = true)
            @RequestBody ReviewLikeRequest reviewLikeRequest
    ) {
        return ResponseEntity.ok(reviewFacade.addLikes(reviewLikeRequest));
    }

    @PostMapping("/comment")
    public ResponseEntity<?> addComment(
            @ApiParam(value = "리뷰 커멘트")
            @RequestBody CommentRequest commentRequest
    ) {
        return reviewFacade.addComment(commentRequest);
    }

    @PatchMapping("/comment")
    public ResponseEntity<?> updateComment(
            @ApiParam(value = "리뷰 커멘트 수정")
            @RequestBody CommentRequest commentRequest
    ) {
        return reviewFacade.updateComment(commentRequest);
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteComment(
            @ApiParam(value = "리뷰 커멘트 수정")
            @RequestBody CommentRequest commentRequest
    ) {
        return reviewFacade.deleteComment(commentRequest);
    }


}
