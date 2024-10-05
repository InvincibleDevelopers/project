package invincibleDevs.bookpago.readingClub.service;

import invincibleDevs.bookpago.profile.ProfileService;
import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubMapRequest;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubRequest;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMap;
import invincibleDevs.bookpago.readingClub.repository.ReadingClubMapRepository;
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
    private final ReadingClubMapService readingClubMapService;
    private final ProfileService profileService;
    private final ReadingClubRepository readingClubRepository;
    private final ReadingClubMapRepository readingClubMapRepository;

    public Page<ReadingClubDto> getClubs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReadingClub> readingClubsPage = readingClubRepository.findAll(pageable);

        List<ReadingClubDto> clubDtos = readingClubsPage.getContent().stream()
                                                        .map(readingClub -> new ReadingClubDto(
                                                                readingClub.getId(),
                                                                readingClub.getClubMembers().size(),
                                                                readingClub.getClubName(),
                                                                readingClub.getLocation(),
                                                                readingClub.getDescription(),
                                                                readingClub.getTime(),
                                                                readingClub.getRepeatCycle(),
                                                                readingClub.getWeekDay()))
                                                        .collect(Collectors.toList());

        return new PageImpl<>(clubDtos, pageable, readingClubsPage.getTotalElements());
    }

    public ReadingClubDto getClub(Long clubId) {
        ReadingClub readingClub = readingClubService.findById(clubId);
        // Dto 변환 코드 (레코드 타입 사용)
        // ReadingClubMap의 갯수 구하기
        return new ReadingClubDto(
                readingClub.getId(),
                readingClub.getClubMembers().size(),
                readingClub.getClubName(),
                readingClub.getLocation(),
                readingClub.getDescription(),
                readingClub.getTime(),
                readingClub.getRepeatCycle(),
                readingClub.getWeekDay()
        );
    }

    public ReadingClubDto createClub(ReadingClubRequest readingClubRequest) {
        Profile admin = profileService.findByKakaoId(readingClubRequest.kakaoId());
        // 새로운 ReadingClub을 생성하기 위해, clubMembers를 Set으로 만듦
        Set<ReadingClubMap> clubMembers = new HashSet<>();
        ReadingClub readingClub = readingClubService.createClub(readingClubRequest, clubMembers);
        clubMembers.add(
                readingClubMapService.create_admin(admin,
                        readingClub)); // 단일 ReadingClubMap 객체를 Set에 추가

        ReadingClubDto readingClubDto = new ReadingClubDto(
                readingClub.getId(), clubMembers.size(),
                readingClubRequest.clubName(), readingClubRequest.location(),
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
        if (readingClubMapRepository.findByIdAndAdmin(clubId, applicant).isPresent()
                || readingClubMapRepository.findByIdAndMember(clubId, applicant).isPresent()
                || readingClubMapRepository.findByIdAndApplicant(clubId, applicant).isPresent()) {
            response.put("success", false);
            return response;
        }

        System.out.println(applicant.getId() + " " + readingClub.getId());
        readingClubMapService.create_applicant(applicant, readingClub);

        response.put("success", true);
        return response;
    }

    public Map<String, Boolean> leaveClub(Long clubId,
            ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();

        Profile user = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        Optional<ReadingClubMap> clubMapOptional = readingClubMapRepository.findByIdAndMember(
                clubId, user);
        // 독서 모임의 멤버나 대기자가 아님
        if (clubMapOptional.isEmpty()) {
            clubMapOptional = readingClubMapRepository.findByIdAndApplicant(clubId, user);
        }
        if (clubMapOptional.isEmpty()) {
            response.put("success", false);
            return response;
        }

        readingClubMapRepository.delete(clubMapOptional.get());

        response.put("success", true);
        return response;
    }

    public Map<String, Boolean> banishClub(Long clubId,
            ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();

        Profile admin = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        // 사용자가 해당 독서 모임의 관리자가 아님
        if (readingClubMapRepository.findByIdAndAdmin(clubId, admin).isEmpty()) {
            response.put("success", false);
            return response;
        }
        for (Long memberId : readingClubMapRequest.memberIds()) {
            Profile member = profileService.findByKakaoId(memberId);
            Optional<ReadingClubMap> clubMapOptional = readingClubMapRepository.findByIdAndMember(
                    clubId, member);
            if (clubMapOptional.isPresent()) {
                readingClubMapRepository.delete(clubMapOptional.get());
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
        if (readingClubMapRepository.findByIdAndAdmin(clubId, admin).isEmpty()) {
            response.put("success", false);
            return response;
        }
        for (Long applicantId : readingClubMapRequest.applicantIds()) {
            Profile applicant = profileService.findByKakaoId(applicantId);
            Optional<ReadingClubMap> clubMapOptional = readingClubMapRepository.findByIdAndApplicant(
                    clubId, applicant);
            if (clubMapOptional.isPresent()) {
                readingClubMapRepository.delete(clubMapOptional.get());
            }
        }

        response.put("success", true);
        return response;
    }

    public Map<String, Boolean> acceptApplicants(Long clubId,
            ReadingClubMapRequest readingClubMapRequest) {
        Map<String, Boolean> response = new HashMap<>();

        Profile admin = profileService.findByKakaoId(readingClubMapRequest.kakaoId());
        // 사용자가 해당 독서 모임의 관리자가 아님
        if (readingClubMapRepository.findByIdAndAdmin(clubId, admin).isEmpty()) {
            response.put("success", false);
            return response;
        }
        for (Long applicantId : readingClubMapRequest.applicantIds()) {
            Profile applicant = profileService.findByKakaoId(applicantId);
            Optional<ReadingClubMap> clubMapOptional = readingClubMapRepository.findByIdAndApplicant(
                    clubId, applicant);
            if (clubMapOptional.isPresent()) {
                ReadingClubMap clubMap = clubMapOptional.get();
                clubMap.setClubMember(applicant);
                clubMap.setClubApplicant(null);
                readingClubMapRepository.save(clubMap);
            }
        }

        response.put("success", true);
        return response;
    }
}
