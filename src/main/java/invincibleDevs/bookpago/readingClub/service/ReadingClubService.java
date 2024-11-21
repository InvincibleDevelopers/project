package invincibleDevs.bookpago.readingClub.service;

import invincibleDevs.bookpago.common.GeometryUtil;
import invincibleDevs.bookpago.common.Location;
import invincibleDevs.bookpago.common.exception.CustomException;
import invincibleDevs.bookpago.readingClub.ReadingClubDistance;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubRequest;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMembers;
import invincibleDevs.bookpago.readingClub.repository.ReadingClubRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

    public ReadingClubDto getClub(Long clubId) {
        ReadingClub readingClub = readingClubRepository.findById(clubId)
                                                       .orElseThrow(() -> new CustomException(
                                                               "Not Found Club."));
//        Optional<ReadingClubMembers> adminOptional = readingClubMembersRepository.findAdminByClubId(
//                clubId);
//        Long adminId = 0L;
//        if (adminOptional.isPresent()) {
//            adminId = adminOptional.get().getClubMember().getUserEntity().getKakaoId();
//        }

        return new ReadingClubDto(
                readingClub.getId(),
                readingClub.getMemberCount(),
                readingClub.getClubName(),
                readingClub.getLocation().getY(),
                readingClub.getLocation().getX(),
                readingClub.getDescription(),
                readingClub.getTime(),
                readingClub.getRepeatCycle(),
                readingClub.getWeekDay()
//                readingClubMembersService.getMemberProfiles(clubId),
//                readingClubMembersService.getApplicantProfiles(clubId)
        );
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

    public List<ReadingClubDto> findClubsListByLocationOrderbyDistance(double latitude,
            double longitude, int page,
            int size) {
        Location location = new Location(latitude, longitude);//현재위치

        Location northeast = ReadingClubDistance.aroundUserNortheastDot(location);
        Location southwest = ReadingClubDistance.aroundUserSouthwestDot(location);

        List<ReadingClub> readingClubs = readingClubRepository.findByLocationOrderByDistance(
                northeast,
                southwest, page, size, location);

        if (readingClubs.isEmpty()) {
            throw new CustomException("No Reading Clubs found.");
        }

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

    public List<ReadingClubDto> findClubListByLocation(Location location) {

        Location northeast = ReadingClubDistance.aroundUserNortheastDot(location);
        Location southwest = ReadingClubDistance.aroundUserSouthwestDot(location);

        List<ReadingClub> readingClubs = readingClubRepository.findByLocation(northeast,
                southwest);

        sortStoresByDistance(readingClubs, location);

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


}
