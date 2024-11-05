package invincibleDevs.bookpago.review;

import invincibleDevs.bookpago.common.exception.CustomException;
import invincibleDevs.bookpago.profile.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public boolean isAuthorOfComment(Comment comment, Profile profile) {
        return commentRepository.existsByIdAndCommentAuthorId(comment.getId(),
                profile.getId());
    }


    public void addComment(Review review, Profile profile, String content) {
        Comment comment = Comment.builder()
                                 .commentAuthor(profile)
                                 .review(review)
                                 .content(content)
                                 .build();
        commentRepository.save(comment);
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                                .orElseThrow(() -> new CustomException("CommentId :"
                                        + commentId
                                        + " Is not found"));
    }

    public void updateComment(Comment comment, String content) {
        Comment updatedComment = comment.toBuilder()
                                        .content(content)
                                        .build();
        commentRepository.save(updatedComment);
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

}
