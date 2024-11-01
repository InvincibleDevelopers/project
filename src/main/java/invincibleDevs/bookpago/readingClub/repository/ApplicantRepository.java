package invincibleDevs.bookpago.readingClub.repository;

import invincibleDevs.bookpago.readingClub.model.Applicant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    // 특정 프로필과 리딩클럽을 기준으로 Applicant 조회
    @Query("SELECT a FROM Applicant a WHERE a.applicant.id = :profileId AND a.readingClub.id = :readingClubId")
    Optional<Applicant> findByApplicantAndReadingClub(@Param("profile") Long profileId,
            @Param("readingClub") Long readingClubId);
}
