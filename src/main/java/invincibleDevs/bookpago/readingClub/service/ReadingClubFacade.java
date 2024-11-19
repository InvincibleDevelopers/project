package invincibleDevs.bookpago.readingClub.service;

import invincibleDevs.bookpago.common.exception.CustomException;
import invincibleDevs.bookpago.profile.ProfileService;
import invincibleDevs.bookpago.profile.Profile;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubMapRequest;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubRequest;
import invincibleDevs.bookpago.readingClub.model.Applicant;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMembers;
import invincibleDevs.bookpago.readingClub.repository.ApplicantRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadingClubFacade {

    private final ReadingClubService readingClubService;
    private final ReadingClubMembersService readingClubMembersService;
    private final ProfileService profileService;
    private final ReadingClubRepository readingClubRepository;
    private final ReadingClubMembersRepository readingClubMembersRepository;
    private final ApplicantRepository applicantRepository;

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
                readingClub.getLocation().getY(),
                readingClubRequest.description(), readingClubRequest.time(),
                readingClubRequest.repeatCycle(), readingClubRequest.weekDay()
        );

        return readingClubDto;
    }

    public Map<String, Boolean> joinClub(Long clubId, ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();

        ReadingClub readingClub = readingClubService.findById(clubId);
        Profile applicant = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        // 이미 독서 모임에 존재하므로 가입 실패
        if (readingClubMembersRepository.findByIdAndAdmin(clubId, applicant).isPresent()
                || readingClubMembersRepository.findByIdAndMember(clubId, applicant).isPresent()) {
            response.put("success", false);
            return response;
        }

        Applicant applicantRequest = Applicant.builder()
                                              .applicant(applicant)
                                              .readingClub(readingClub)
                                              .build();

        applicantRepository.save(applicantRequest);

        response.put("success", true);
        return response;
    }

    public Map<String, Boolean> leaveClub(Long clubId,
            ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();

        Profile user = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        Optional<ReadingClubMembers> clubMapOptional = readingClubMembersRepository.findByIdAndMember(
                clubId, user);
        // 독서 모임의 멤버나 대기자가 아님
//        if (clubMapOptional.isEmpty()) {
//            clubMapOptional = readingClubMembersRepository.findByIdAndApplicant(clubId, user);
//        }
        if (clubMapOptional.isEmpty()) {
            response.put("success", false);
            return response;
        }

        readingClubMembersRepository.delete(clubMapOptional.get());

        response.put("success", true);
        return response;
    }

    public Map<String, Boolean> banishClub(Long clubId,
            ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();

        Profile admin = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        // 사용자가 해당 독서 모임의 관리자가 아님
        if (readingClubMembersRepository.findByIdAndAdmin(clubId, admin).isEmpty()) {
            response.put("success", false);
            return response;
        }
        for (Long memberId : readingClubMapRequest.members()) {
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
        for (Long applicantId : readingClubMapRequest.applicants()) {
            Profile applicant = profileService.findByKakaoId(applicantId);

            applicantRepository.findByApplicantAndReadingClub(applicantId, clubId)
                               .ifPresentOrElse(
                                       applicantRepository::delete,
                                       () -> {
                                           throw new IllegalArgumentException(
                                                   "Applicant not found with specified Profile and ReadingClub");
                                       }
                               );


        }

        response.put("success", true);
        return response;
    }

    public Map<String, Boolean> acceptApplicants(Long clubId,
            ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();
        ReadingClub readingClub = readingClubRepository.findById(clubId).get();
        Profile admin = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        // 사용자가 해당 독서 모임의 관리자가 아님
        if (readingClubMembersRepository.findByIdAndAdmin(clubId, admin).isEmpty()) {
            response.put("success", false);
            return response;
        }
        for (Long applicantId : readingClubMapRequest.applicants()) {
            Profile applicant = profileService.findByKakaoId(applicantId);

            applicantRepository.findByApplicantAndReadingClub(applicantId, clubId)
                               .ifPresentOrElse(applicantRequest -> {
                                           ReadingClubMembers newMember = ReadingClubMembers.builder()
                                                                                            .clubMember(
                                                                                                    applicant)
                                                                                            .readingClub(
                                                                                                    readingClub)
                                                                                            .build();
                                           readingClubMembersRepository.save(newMember);
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

    public ResponseEntity<?> getNearByClubs(double latitude, double longitude, int page, int size) {
        try {
            return ResponseEntity.ok(
                    readingClubService.findClubsListByLocationOrderbyDistance(latitude, longitude,
                            page,
                            size));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
