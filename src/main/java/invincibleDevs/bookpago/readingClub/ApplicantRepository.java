package invincibleDevs.bookpago.readingClub;

import invincibleDevs.bookpago.readingClub.model.Applicant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    // ReadingClub ID로 Applicant 목록 조회
    Optional<List<Applicant>> findByReadingClub_Id(Long clubId);

    Optional<Applicant> findByApplicantAndReadingClub(Long applicantId, Long clubId);
}
