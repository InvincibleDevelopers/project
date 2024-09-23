package invincibleDevs.bookpago.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query(value = "SELECT id, content, isbn, rating, author_profile_id FROM review WHERE author_profile_id = :profileId ORDER BY id ASC LIMIT :size", nativeQuery = true)
    List<Review> findFirstReviewsByLastBookIsbn(@Param("profileId") Long profileId, @Param("size") int size);

//    @Query(value = "SELECT CAST(id AS UNSIGNED), content, isbn, rating, CAST(author_profile_id AS UNSIGNED) " +
//            "FROM review WHERE author_profile_id = :profileId ORDER BY id ASC LIMIT :size", nativeQuery = true)
//    List<Object[]> findFirstBooksByLastBookId(@Param("profileId") Long profileId, @Param("size") int size);



    @Query(value = "SELECT id, content, isbn, rating, author_profile_id FROM review WHERE author_profile_id = :profileId AND isbn > :lastBookIsbn ORDER BY id ASC LIMIT :size", nativeQuery = true)
    List<Review> findReviewsByLastBookIsbnAfterCursor(@Param("profileId") Long profileId, @Param("lastBookIsbn") Long lastBookIsbn, @Param("size") int size);



}
