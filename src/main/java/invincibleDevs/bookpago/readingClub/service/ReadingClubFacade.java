package invincibleDevs.bookpago.readingClub.service;

import invincibleDevs.bookpago.profile.ProfileService;
import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubMapRequest;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubRequest;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubDetailDto;
import invincibleDevs.bookpago.readingClub.model.ReadingClubApplicants;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMembers;
import invincibleDevs.bookpago.readingClub.repository.ReadingClubApplicantsRepository;
import invincibleDevs.bookpago.readingClub.repository.ReadingClubMembersRepository;
import invincibleDevs.bookpago.readingClub.repository.ReadingClubRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadingClubFacade {

    private final ReadingClubService readingClubService;
    private final ReadingClubMembersService readingClubMembersService;
    private final ProfileService profileService;
    private final ReadingClubRepository readingClubRepository;
    private final ReadingClubMembersRepository readingClubMembersRepository;
    private final ReadingClubApplicantsRepository readingClubApplicantsRepository;

    public Page<ReadingClubDto> getClubs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReadingClub> readingClubsPage = readingClubRepository.findAllWithWeekDays(pageable);

        List<ReadingClubDto> clubDtos = readingClubsPage.getContent().stream()
                                                        .map(readingClub -> new ReadingClubDto(
                                                                readingClub.getId(),
                                                                readingClub.getMemberCount(),
                                                                readingClub.getClubName(),
                                                                readingClub.getLocation(),
                                                                readingClub.getDescription(),
                                                                readingClub.getTime(),
                                                                readingClub.getRepeatCycle(),
                                                                readingClub.getWeekDay()))
                                                        .collect(Collectors.toList());

        return new PageImpl<>(clubDtos, pageable, readingClubsPage.getTotalElements());
    }

    public ReadingClubDetailDto getClub(Long clubId) {
        ReadingClub readingClub = readingClubService.findById(clubId);
        Optional<ReadingClubMembers> adminOptional = readingClubMembersRepository.findAdminByClubId(clubId);
        Long adminId = 0L;
        if (adminOptional.isPresent())
            adminId = adminOptional.get().getClubMember().getUserEntity().getKakaoId();

        return new ReadingClubDetailDto(
                readingClub.getId(),
                adminId,
                readingClub.getMemberCount(),
                readingClub.getClubName(),
                readingClub.getLocation(),
                readingClub.getDescription(),
                readingClub.getTime(),
                readingClub.getRepeatCycle(),
                readingClub.getWeekDay(),
                readingClubMembersService.getMemberProfiles(clubId),
                readingClubMembersService.getApplicantProfiles(clubId)
        );
    }

    public ReadingClubDto createClub(ReadingClubRequest readingClubRequest) {
        Profile admin = profileService.findByKakaoId(readingClubRequest.kakaoId());
        // 새로운 ReadingClub을 생성하기 위해, clubMembers를 Set으로 만듦
        Set<ReadingClubMembers> clubMembers = new HashSet<>();
        ReadingClub readingClub = readingClubService.createClub(readingClubRequest, clubMembers);
        clubMembers.add(
                readingClubMembersService.create_admin(admin,
                        readingClub)); // 단일 ReadingClubMap 객체를 Set에 추가

        return new ReadingClubDto(
                readingClub.getId(),
                readingClub.getMemberCount(),
                readingClubRequest.clubName(), readingClubRequest.location(),
                readingClubRequest.description(), readingClubRequest.time(),
                readingClubRequest.repeatCycle(), readingClubRequest.weekDay()
        );
    }

    public Map<String, Boolean> joinClub(Long clubId, ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();

        ReadingClub readingClub = readingClubService.findById(clubId);
        Profile applicant = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        // 이미 독서 모임에 존재하므로 가입 실패
        if (readingClubMembersRepository.findByIdAndMember(clubId, applicant).isPresent()) {
            response.put("success", false);
            return response;
        }

        ReadingClubApplicants applicantRequest = ReadingClubApplicants.builder()
                                              .clubApplicant(applicant)
                                              .readingClub(readingClub)
                                              .build();

        readingClubApplicantsRepository.save(applicantRequest);

        response.put("success", true);
        return response;
    }

    public Map<String, Boolean> leaveClub(Long clubId, ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();

        Profile member = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        Optional<ReadingClubMembers> memberOptional = readingClubMembersRepository.findByIdAndMember(
                clubId, member);

        if (memberOptional.isEmpty()) {
            response.put("success", false);
            return response;
        }

        ReadingClub readingClub = memberOptional.get().getReadingClub();
        readingClub.removeMember();
        readingClubRepository.save(readingClub);
        readingClubMembersRepository.delete(memberOptional.get());
        response.put("success", true);

        return response;
    }

    public Map<String, Boolean> banishClub(Long clubId, ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();

        Profile admin = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        // 사용자가 해당 독서 모임의 관리자가 아님
        if (readingClubMembersRepository.findByIdAndAdmin(clubId, admin).isEmpty()) {
            response.put("success", false);
            return response;
        }
        for (Long memberId : readingClubMapRequest.members()) {
            Profile member = profileService.findByKakaoId(memberId);
            Optional<ReadingClubMembers> memberOptional = readingClubMembersRepository.findByIdAndMember(
                    clubId, member);
            if (memberOptional.isPresent()) {
                ReadingClub readingClub = memberOptional.get().getReadingClub();
                readingClub.removeMember();
                readingClubRepository.save(readingClub);
                readingClubMembersRepository.delete(memberOptional.get());
            }
        }

        response.put("success", true);
        return response;
    }

    public Map<String, Boolean> rejectApplicants(Long clubId, ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();

        Profile admin = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        // 사용자가 해당 독서 모임의 관리자가 아님
        if (readingClubMembersRepository.findByIdAndAdmin(clubId, admin).isEmpty()) {
            response.put("success", false);
            return response;
        }
        for (Long applicantId : readingClubMapRequest.applicants()) {
            Profile applicant = profileService.findByKakaoId(applicantId);
            Optional<ReadingClubApplicants> applicantOptional = readingClubApplicantsRepository.findByIdAndApplicant(
                    clubId, applicant);
            applicantOptional.ifPresent(readingClubApplicantsRepository::delete);
        }

        response.put("success", true);
        return response;
    }

    public Map<String, Boolean> acceptApplicants(Long clubId, ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();

        ReadingClub readingClub = readingClubService.findById(clubId);
        Profile admin = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        // 사용자가 해당 독서 모임의 관리자가 아님
        if (readingClubMembersRepository.findByIdAndAdmin(clubId, admin).isEmpty()) {
            response.put("success", false);
            return response;
        }
        for (Long applicantId : readingClubMapRequest.applicants()) {
            Profile applicant = profileService.findByKakaoId(applicantId);

            readingClubApplicantsRepository.findByIdAndApplicant(clubId, applicant)
                               .ifPresentOrElse(applicantRequest -> {
                                           ReadingClubMembers newMember = ReadingClubMembers.builder()
                                                                                            .clubMember(applicant)
                                                                                            .readingClub(readingClub)
                                                                                            .build();
                                           readingClubMembersRepository.save(newMember);
                                           readingClub.addMember();
                                           readingClubRepository.save(readingClub);
                                           readingClubApplicantsRepository.delete(applicantRequest);
                                           },
                                       () -> {
                                           throw new IllegalArgumentException(
                                                   "Applicant not found with specified Profile and ReadingClub");
                                       }
                               );
        }
        response.put("success", true);

        return response;
    }

    public Page<ReadingClubDto> getUserClubs(Long kakaoId, int page, int size) {
        Profile user = profileService.findByKakaoId(kakaoId);
        return readingClubMembersService.getUserClubs(user, page, size);
    }
}
