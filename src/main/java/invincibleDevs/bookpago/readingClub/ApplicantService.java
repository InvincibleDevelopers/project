package invincibleDevs.bookpago.readingClub;

import invincibleDevs.bookpago.common.exception.CustomException;
import invincibleDevs.bookpago.profile.ProfileDTO;
import invincibleDevs.bookpago.readingClub.model.Applicant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantRepository applicantRepository;

    public List<ProfileDTO> getApplicants(Long clubId) {
        // Applicant 리스트 조회
        List<Applicant> applicants = applicantRepository.findByReadingClub_Id(clubId)
                                                        .orElse(Collections.emptyList());

        return applicants.stream()
                         .map(this::convertToProfileDTO)
                         .collect(Collectors.toList());
    }

    private ProfileDTO convertToProfileDTO(Applicant applicant) {
        return new ProfileDTO(
                applicant.getApplicant().getUserEntity().getKakaoId(),
                applicant.getApplicant().getNickName(),
                applicant.getApplicant().getProfileImgUrl()
        );
    }

    public void createApplicant(Applicant applicantRequest) {
        applicantRepository.save(applicantRequest);
    }

    public Applicant findByApplicantAndReadingClub(Long applicantId, Long clubId) {
        return applicantRepository.findByApplicantAndReadingClub(applicantId, clubId)
                                  .orElseThrow(() -> new CustomException("not found applicant."));
    }

    public void delete(Object o) {
    }
}
