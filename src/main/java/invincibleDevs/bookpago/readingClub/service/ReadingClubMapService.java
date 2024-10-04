package invincibleDevs.bookpago.readingClub.service;

import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMap;
import invincibleDevs.bookpago.readingClub.repository.ReadingClubMapRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadingClubMapService {

    private final ReadingClubMapRepository readingClubMapRepository;
    private final ReadingClubService readingClubService;

    public Page<ReadingClubDto> getUserClubs(Profile profile, int page, int size) {
        // 관리자와 멤버로 검색하여 리스트 가져오기
        List<ReadingClubMap> adminClubs = readingClubMapRepository.findByClubAdmin(profile);
        List<ReadingClubMap> memberClubs = readingClubMapRepository.findByClubMember(profile);

        // 두 리스트를 합치기 위해 새로운 리스트 생성
        List<ReadingClubDto> readingClubDtoList = new ArrayList<>();

        // adminClubs 리스트를 Dto로 변환 후 추가
        readingClubDtoList.addAll(adminClubs.stream()
                                            .map(club -> new ReadingClubDto(
                                                    club.getReadingClub().getId(),
                                                    readingClubService.getMemberCount(club.getReadingClub()),
                                                    club.getReadingClub().getClubName(),
                                                    club.getReadingClub().getLocation(),
                                                    club.getReadingClub().getDescription(),
                                                    club.getReadingClub().getTime(),
                                                    club.getReadingClub().getRepeatCycle(),
                                                    club.getReadingClub().getWeekDay()))
                                            .collect(Collectors.toList()));

        // memberClubs 리스트를 Dto로 변환 후 추가
        readingClubDtoList.addAll(memberClubs.stream()
                                             .map(club -> new ReadingClubDto(
                                                     club.getReadingClub().getId(),
                                                     readingClubService.getMemberCount(club.getReadingClub()),
                                                     club.getReadingClub().getClubName(),
                                                     club.getReadingClub().getLocation(),
                                                     club.getReadingClub().getDescription(),
                                                     club.getReadingClub().getTime(),
                                                     club.getReadingClub().getRepeatCycle(),
                                                     club.getReadingClub().getWeekDay()))
                                             .collect(Collectors.toList()));

        // 페이징 정보 설정
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), readingClubDtoList.size());

        // 유효한 범위 내의 결과 리스트만 추출
        List<ReadingClubDto> content = (start > readingClubDtoList.size())
                ? new ArrayList<>()
                : readingClubDtoList.subList(start, end);
        System.out.println(content.size());

        // 페이징된 결과 리스트 반환
        return new PageImpl<>(content);
    }

    public ReadingClubMap create_admin(Profile admin, ReadingClub readingClub) {
        ReadingClubMap readingClubMap = ReadingClubMap.builder()
                                                      .clubAdmin(admin)
                                                      .readingClub(readingClub)
                                                      .build();
        return readingClubMapRepository.save(readingClubMap);
    }

    public ReadingClubMap create_applicant(Profile applicant, ReadingClub readingClub) {
        ReadingClubMap readingClubMap = ReadingClubMap.builder()
                .clubApplicant(applicant)
                .readingClub(readingClub)
                .build();
        return readingClubMapRepository.save(readingClubMap);
    }

    public ReadingClubMap create_member(Profile member, ReadingClub readingClub) {
        ReadingClubMap readingClubMap = ReadingClubMap.builder()
                .clubMember(member)
                .readingClub(readingClub)
                .build();
        return readingClubMapRepository.save(readingClubMap);
    }

    public ReadingClubMap delete(Profile admin, ReadingClub readingClub) {
        ReadingClubMap readingClubMap = ReadingClubMap.builder()
                .clubAdmin(admin)
                .readingClub(readingClub)
                .build();
        return readingClubMapRepository.save(readingClubMap);
    }
}
