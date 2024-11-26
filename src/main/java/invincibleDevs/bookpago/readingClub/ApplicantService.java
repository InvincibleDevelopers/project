package invincibleDevs.bookpago.readingClub;

import invincibleDevs.bookpago.common.exception.CustomException;
import invincibleDevs.bookpago.profile.Profile;
import invincibleDevs.bookpago.profile.ProfileDTO;
import invincibleDevs.bookpago.readingClub.model.Applicant;
import invincibleDevs.bookpago.readingClub.repository.ApplicantRepository;
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
//        List<Applicant> applicants = applicantRepository.findByReadingClub_Id(clubId)
//                                                        .orElse(Collections.emptyList());

        List<Applicant> applicants = applicantRepository.findApplicantsWithProfileByReadingClubId(
                                                                clubId)
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
                                  .orElseThrow(() -> new CustomException(
                                          "Applicant not found for applicantId: " + applicantId
                                                  + " and clubId: " + clubId));
    }

    public void delete(Applicant applicant) {
        applicantRepository.delete(applicant);
    }

    public Applicant findById(Long applicantId) {
        return applicantRepository.findById(applicantId)
                                  .orElseThrow(() -> new CustomException("not found applicant."));
    }

    public List<Applicant> findByReadingClubId(Long clubId) {
        return applicantRepository.findByReadingClub_Id(clubId)
                                  .orElseThrow(() -> new CustomException("해당 클럽의 신청자가 없습니다."));
    }

    public Applicant findByApplicant(Profile applicantProfile) {
        return applicantRepository.findByApplicant(applicantProfile)
                                  .orElseThrow(() -> new CustomException("해당 카카오 아이디의 신청자가 없습니다."));
    }
}
