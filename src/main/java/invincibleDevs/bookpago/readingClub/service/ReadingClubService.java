package invincibleDevs.bookpago.readingClub.service;

import invincibleDevs.bookpago.common.GeometryUtil;
import invincibleDevs.bookpago.common.Location;
import invincibleDevs.bookpago.readingClub.ReadingClubDistance;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubRequest;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMembers;
import invincibleDevs.bookpago.readingClub.repository.ReadingClubRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadingClubService {

    private final ReadingClubRepository readingClubRepository;

    public ReadingClub findById(Long clubId) {
        return readingClubRepository.findById(clubId)
                                    .orElse(null);
    }

    public ReadingClub createClub(ReadingClubRequest readingClubRequest,
            Set<ReadingClubMembers> members) {
        // Point 객체 생성
        Point location = GeometryUtil.createPoint(readingClubRequest.latitude(),
                readingClubRequest.longitude());

        ReadingClub readingClub = ReadingClub.builder()
                                             .clubMembers(members)
                                             .clubName(readingClubRequest.clubName())
                                             .description(readingClubRequest.description())
                                             .location(location)
                                             .time(readingClubRequest.time())
                                             .repeatCycle(readingClubRequest.repeatCycle())
                                             .weekDay(readingClubRequest.weekDay())
                                             .build();
        readingClub.addMember();
        return readingClubRepository.save(readingClub);
    }

    public List<ReadingClubDto> findStoresListByLocation(Location location) {

        Location northeast = ReadingClubDistance.aroundUserNortheastDot(location);
        Location southwest = ReadingClubDistance.aroundUserSouthwestDot(location);

        List<ReadingClub> readingClubs = readingClubRepository.findByLocation(northeast,
                southwest);

        sortStoresByDistance(readingClubs, location);
        System.out.println("&&&&&&&&&&&&&&&");
        // ID 출력
        System.out.println(
                readingClubs.stream()
                            .map(ReadingClub::getId) // 각 ReadingClub 객체에서 ID 추출
                            .collect(Collectors.toList()) // ID를 리스트로 변환
        );

        List<ReadingClubDto> readingClubDtos = readingClubs.stream()
                                                           .map(readingClub -> new ReadingClubDto(
                                                                   readingClub.getId(),
                                                                   readingClub.getMemberCount(),
                                                                   readingClub.getClubName(),
                                                                   readingClub.getLocation().getY(),
                                                                   readingClub.getLocation().getX(),
                                                                   readingClub.getDescription(),
                                                                   readingClub.getTime(),
                                                                   readingClub.getRepeatCycle(),
                                                                   readingClub.getWeekDay()
                                                           ))
                                                           .collect(Collectors.toList());

        return readingClubDtos;
    }

    /**
     * 사용자 위치 기준으로 가장 가까운순으로 정렬합니다.
     */
    private void sortStoresByDistance(List<ReadingClub> readingClubs, Location location) {

        readingClubs.sort((o1, o2) -> (int) (ReadingClubDistance.calculateDistance(location, o1)
                - ReadingClubDistance.calculateDistance(location, o2)));
    }

    public List<ReadingClub> testSort() {
        // GeometryFactory 인스턴스 생성
        GeometryFactory geometryFactory = new GeometryFactory();

        // 기준 위치 설정 (위도, 경도)
        Location location = new Location(30, 120); // 기준 위치 (30, 120)

        // 테스트 데이터 생성 (위치 간 거리 비교가 명확하도록 설정)
        List<ReadingClub> readingClubs = new ArrayList<>(List.of(
                ReadingClub.builder()
                           .id(1L)
                           .clubName("Club1")
                           .location(
                                   geometryFactory.createPoint(new Coordinate(40, 130))) // 가장 먼 위치
                           .description("Description 1")
                           .time("10:00 AM")
                           .repeatCycle(7)
                           .memberCount(10)
                           .weekDay(List.of(1, 2, 3))
                           .build(),

                ReadingClub.builder()
                           .id(2L)
                           .clubName("Club2")
                           .location(geometryFactory.createPoint(new Coordinate(35, 125))) // 중간 거리
                           .description("Description 2")
                           .time("02:00 PM")
                           .repeatCycle(7)
                           .memberCount(5)
                           .weekDay(List.of(1, 4, 5))
                           .build(),

                ReadingClub.builder()
                           .id(3L)
                           .clubName("Club3")
                           .location(
                                   geometryFactory.createPoint(
                                           new Coordinate(31, 121))) // 가장 가까운 위치
                           .description("Description 3")
                           .time("06:00 PM")
                           .repeatCycle(7)
                           .memberCount(20)
                           .weekDay(List.of(2, 3, 6))
                           .build()
        ));
        readingClubs.stream().forEach(value -> System.out.println(value.getId()));

        // 정렬
        sortStoresByDistance(readingClubs, location);

        readingClubs.stream().forEach(value -> System.out.println(value.getId()));

        // 정렬된 리스트 반환
        return readingClubs;
    }


}
