package invincibleDevs.bookpago.review;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 쿼리 메서드를 사용한 방법
    boolean existsByIdAndCommentAuthorId(Long commentId, Long authorId);

}
