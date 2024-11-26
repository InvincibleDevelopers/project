package invincibleDevs.bookpago.readingClub.service;

import invincibleDevs.bookpago.common.exception.CustomException;
import invincibleDevs.bookpago.profile.Profile;
import invincibleDevs.bookpago.profile.ProfileDTO;
import invincibleDevs.bookpago.profile.ProfileService;
import invincibleDevs.bookpago.readingClub.ApplicantService;
import invincibleDevs.bookpago.readingClub.ClubWithMemberDto;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubMapRequest;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubRequest;
import invincibleDevs.bookpago.readingClub.model.Applicant;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMembers;
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
    private final ApplicantService applicantService;

    public Page<ReadingClubDto> getClubs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReadingClub> readingClubsPage = readingClubRepository.findAllWithWeekDays(pageable);

        List<ReadingClubDto> clubDtos = readingClubsPage.getContent().stream()
                                                        .map(readingClub -> new ReadingClubDto(
                                                                readingClub.getId(),
                                                                readingClub.getMemberCount(),
                                                                readingClub.getClubName(),
                                                                readingClub.getLocation().getX(),
                                                                readingClub.getLocation().getY(),
                                                                readingClub.getAddress(),
                                                                readingClub.getDescription(),
                                                                readingClub.getTime(),
                                                                readingClub.getRepeatCycle(),
                                                                readingClub.getWeekDay()))
                                                        .collect(Collectors.toList());

        return new PageImpl<>(clubDtos, pageable, readingClubsPage.getTotalElements());
    }

    public ReadingClubDto createClub(ReadingClubRequest readingClubRequest) {
        Profile admin = profileService.findByKakaoId(readingClubRequest.kakaoId());
        // 새로운 ReadingClub을 생성하기 위해, clubMembers를 Set으로 만듦
        Set<ReadingClubMembers> clubMembers = new HashSet<>();
        ReadingClub readingClub = readingClubService.createClub(readingClubRequest, clubMembers);
        clubMembers.add(
                readingClubMembersService.create_admin(admin,
                        readingClub)); // 단일 ReadingClubMap 객체를 Set에 추가

        ReadingClubDto readingClubDto = new ReadingClubDto(
                readingClub.getId(),
                readingClub.getMemberCount(),
                readingClubRequest.clubName(), readingClub.getLocation().getX(),
                readingClub.getLocation().getY(), readingClub.getAddress(),
                readingClubRequest.description(), readingClubRequest.time(),
                readingClubRequest.repeatCycle(), readingClubRequest.weekDay()
        );

        return readingClubDto;
    }

    public Map<String, Boolean> joinClub(Long clubId, ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();

        ReadingClub readingClub = readingClubService.findById(clubId);
        Profile applicantProfile = profileService.findByKakaoId(readingClubMapRequest.kakaoId());

        // 가입 여부 검사
        boolean isAlreadyInClub =
                readingClubMembersRepository.findByIdAndMember(clubId, applicantProfile)
                                            .isPresent();

        if (isAlreadyInClub) {
            response.put("success", false);
            return response;
        }

        Applicant applicantRequest = Applicant.builder()
                                              .applicant(applicantProfile)
                                              .readingClub(readingClub)
                                              .build();
        applicantService.createApplicant(applicantRequest);
        response.put("success", true);
        return response;
    }

    public Map<String, Boolean> leaveClub(Long clubId,
            ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();

        Profile memberProfile = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        Optional<ReadingClubMembers> clubMember = readingClubMembersRepository.findByIdAndMember(
                clubId, memberProfile);

        Applicant applicant;
        //ㅅㅂ applicant에잇으면 그거하고, 없으면 멤버에서삭제;
        // 독서 모임의 멤버나 대기자가 아님
        if (clubMember.isEmpty()) {
            applicant = applicantService.findByApplicantAndReadingClub(memberProfile.getId(),
                    clubId);
            applicantService.delete(applicant);
            response.put("success", true);
            return response;
        }
//        if (applicant.isEmpty()) {
//            response.put("success", false);
//            return response;
//        }

        readingClubMembersRepository.delete(clubMember.get());

        response.put("success", true);
        return response;
    }

    public Map<String, Boolean> banishClub(Long clubId,
            ReadingClubMapRequest readingClubMapRequest) { //멤버 강퇴 기능
        Map<String, Boolean> response = new HashMap<>();

        Profile admin = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        // 사용자가 해당 독서 모임의 관리자가 아님
        if (readingClubMembersRepository.findByIdAndAdmin(clubId, admin).isEmpty()) {
            response.put("success", false);
            return response;
        }
        for (Long memberId : readingClubMapRequest.members().get()) {
            Profile member = profileService.findByKakaoId(memberId);
            Optional<ReadingClubMembers> clubMapOptional = readingClubMembersRepository.findByIdAndMember(
                    clubId, member);
            if (clubMapOptional.isPresent()) {
                readingClubMembersRepository.delete(clubMapOptional.get());
            }
        }

        response.put("success", true);
        return response;
    }

    public Map<String, Boolean> rejectApplicants(Long clubId,
            ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();

        Profile admin = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        // 사용자가 해당 독서 모임의 관리자가 아님
        if (readingClubMembersRepository.findByIdAndAdmin(clubId, admin).isEmpty()) {
            response.put("success", false);
            return response;
        }
        for (Long applicantKakaoId : readingClubMapRequest.applicants().get()) {
            Profile applicantProfile = profileService.findByKakaoId(applicantKakaoId);
            Applicant applicant = applicantService.findByApplicant(applicantProfile);
//            Applicant applicant = applicantService.findByApplicantAndReadingClub(applicantId,
//                    clubId);
            applicantService.delete(applicant);
        }

        response.put("success", true);
        return response;
    }

    public Map<String, Boolean> acceptApplicants(Long clubId,
            ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();
//        ReadingClub readingClub = readingClubRepository.findById(clubId).get();
        ReadingClub readingClub = readingClubService.findById(clubId);
        Profile admin = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        // 사용자가 해당 독서 모임의 관리자가 아님
        if (readingClubMembersRepository.findByIdAndAdmin(clubId, admin).isEmpty()) {
            response.put("success", false);
            return response;
        }
        System.out.println("^^^^^^^^^^^^");
        System.out.println(clubId);
        System.out.println(readingClubMapRequest.applicants());
        List<Applicant> applicants = applicantService.findByReadingClubId(clubId);

        for (Long applicantKakaoId : readingClubMapRequest.applicants().get()) {
            Profile applicantProfile = profileService.findByKakaoId(applicantKakaoId);
            Applicant applicant = applicantService.findByApplicantAndReadingClub(
                    applicantProfile.getId(), clubId);
            System.out.println("여기통과?");

            ReadingClubMembers newMember = ReadingClubMembers.builder()
                                                             .clubMember(
                                                                     applicantProfile)
                                                             .readingClub(
                                                                     readingClub)
                                                             .build();
            readingClubMembersRepository.save(newMember);
//            Applicant applicant = applicantService.findByApplicantAndReadingClub(applicantId,
//                    clubId);
            applicantService.delete(applicant);
        }

        response.put("success", true);
        return response;
    }

    public Page<ReadingClubDto> getUserClubs(Long kakaoId, int page, int size) {
        Profile user = profileService.findByKakaoId(kakaoId);
        return readingClubMembersService.getUserClubs(user, page, size);
    }

    public Map<String, Object> getNearByClubs(double latitude, double longitude, int page,
            int size) {
        try {
            List<ReadingClubDto> clubs = readingClubService.findClubsListByLocationOrderbyDistance(
                    latitude, longitude, page, size);
            return Map.of("content", clubs);
//            return readingClubService.findClubsListByLocationOrderbyDistance(latitude, longitude,
//                    page,
//                    size);

        } catch (CustomException e) {
            throw new CustomException(e.getMessage());
        }
    }

    public ClubWithMemberDto getClub(Long clubId, Long kakaoId) {
        ReadingClubDto readingClubDto = readingClubService.getClub(clubId);
        Profile profile = profileService.findByKakaoId(kakaoId);
        List<Long> memberProfileIdList = readingClubMembersService.getMemberProfileIdList(clubId,
                profile.getId());
        boolean isAdmin = readingClubMembersService.isAdmin(profile.getId(), clubId);
        System.out.println("^^^^^^^^^^^^^^^^^^^^");
        System.out.println(isAdmin);
        ReadingClubMembers adminMember = readingClubMembersService.getAdminProfile(clubId);
        System.out.println(adminMember.getClubMember().getId());
        List<ProfileDTO> memberProfileList = profileService.getProfileDtoList(memberProfileIdList);

        ClubWithMemberDto clubWithMemberDto = new ClubWithMemberDto(isAdmin,
                adminMember.getClubMember().getUserEntity().getKakaoId(),
                readingClubDto, memberProfileList,
                applicantService.getApplicants(clubId));
        return clubWithMemberDto;
    }
}
