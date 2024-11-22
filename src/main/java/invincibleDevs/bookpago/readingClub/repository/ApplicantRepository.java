package invincibleDevs.bookpago.readingClub.repository;

import invincibleDevs.bookpago.readingClub.model.Applicant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    // ReadingClub ID로 Applicant 목록 조회
    Optional<List<Applicant>> findByReadingClub_Id(Long clubId);

//    Optional<Applicant> findByApplicantAndReadingClub(Long applicantId, Long clubId);

    @Query("SELECT a FROM Applicant a WHERE a.applicant.id = :applicantId AND a.readingClub.id = :clubId")
    Optional<Applicant> findByApplicantAndReadingClub(@Param("applicantId") Long applicantId,
            @Param("clubId") Long clubId);

    @Query("SELECT a FROM Applicant a JOIN FETCH a.applicant WHERE a.readingClub.id = :readingClubId")
    Optional<List<Applicant>> findApplicantsWithProfileByReadingClubId(
            @Param("readingClubId") Long readingClubId);


}
