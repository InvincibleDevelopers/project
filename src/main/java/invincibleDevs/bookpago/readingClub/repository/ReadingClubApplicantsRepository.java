package invincibleDevs.bookpago.readingClub.repository;

import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.readingClub.model.ReadingClubApplicants;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadingClubApplicantsRepository extends JpaRepository<ReadingClubApplicants, Long> {
    // 특정 독서 모임(clubId)에서 특정 프로필이 clubApplicant로 있는지 확인
    @Query("SELECT a FROM ReadingClubApplicants a WHERE a.readingClub.id = :clubId AND a.clubApplicant = :profile")
    Optional<ReadingClubApplicants> findByIdAndApplicant(@Param("clubId") Long clubId,
                                                         @Param("profile") Profile profile);

    // 특정 독서 모임(clubId)에서 대기자 리스트 조회
    @Query("SELECT a FROM ReadingClubApplicants a WHERE a.readingClub.id = :clubId AND a.clubApplicant IS NOT NULL")
    List<ReadingClubApplicants> findApplicantsByClubId(@Param("clubId") Long clubId);
}
