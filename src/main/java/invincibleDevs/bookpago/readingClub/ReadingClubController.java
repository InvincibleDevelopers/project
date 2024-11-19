package invincibleDevs.bookpago.readingClub;

import invincibleDevs.bookpago.readingClub.dto.ReadingClubMapRequest;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubRequest;
import invincibleDevs.bookpago.readingClub.service.ReadingClubFacade;
import invincibleDevs.bookpago.readingClub.service.ReadingClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/social")
public class ReadingClubController {

    private final ReadingClubFacade readingClubFacade;
    private final ReadingClubService readingClubService;

    @GetMapping("/clubs")
    public ResponseEntity<?> getClubs(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        try {
            return ResponseEntity.ok(readingClubFacade.getClubs(page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/clubs")
    public ResponseEntity<?> createClub(
            @RequestBody ReadingClubRequest readingClubRequest
    ) {
        try {
            System.out.println(readingClubRequest);
            return ResponseEntity.ok(readingClubFacade.createClub(readingClubRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @GetMapping("/clubs/{clubId}")
//    public ResponseEntity<?> getTargetClub(
//            @PathVariable("clubId") Long clubId
//    ) {
//        return ResponseEntity.ok(readingClubFacade.getClub(clubId));
//    }

    @PostMapping("/clubs/{clubId}/members")
    public ResponseEntity<?> joinClub(
            @PathVariable("clubId") Long clubId,
            @RequestBody ReadingClubMapRequest readingClubMapRequest
    ) {
        try {
            return ResponseEntity.ok(readingClubFacade.joinClub(clubId, readingClubMapRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/clubs/{clubId}/members")
    public ResponseEntity<?> leaveClub(
            @PathVariable("clubId") Long clubId,
            @RequestBody ReadingClubMapRequest readingClubMapRequest
    ) {
        try {
            return ResponseEntity.ok(readingClubFacade.leaveClub(clubId, readingClubMapRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/clubs/{clubId}/admin")
    public ResponseEntity<?> banishClub(
            @PathVariable("clubId") Long clubId,
            @RequestBody ReadingClubMapRequest readingClubMapRequest
    ) {
        try {
            return ResponseEntity.ok(readingClubFacade.banishClub(clubId, readingClubMapRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/clubs/{clubId}/applicants")
    public ResponseEntity<?> rejectApplicants(
            @PathVariable("clubId") Long clubId,
            @RequestBody ReadingClubMapRequest readingClubMapRequest
    ) {
        try {
            return ResponseEntity.ok(
                    readingClubFacade.rejectApplicants(clubId, readingClubMapRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/clubs/{clubId}/applicants")
    public ResponseEntity<?> acceptApplicants(
            @PathVariable("clubId") Long clubId,
            @RequestBody ReadingClubMapRequest readingClubMapRequest
    ) {
        try {
            return ResponseEntity.ok(
                    readingClubFacade.acceptApplicants(clubId, readingClubMapRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/clubs/users/{kakaoId}")
    public ResponseEntity<?> getUserClubs(
            @PathVariable("kakaoId") Long kakaoId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        try {
            return ResponseEntity.ok(readingClubFacade.getUserClubs(kakaoId, page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/clubs/nearby")
    public ResponseEntity<?> getNearByClubs(
            @RequestParam(name = "latitude") double latitude,
            @RequestParam(name = "longitude") double longitude,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(
                readingClubFacade.getNearByClubs(latitude, longitude,
                        page, size));
    }
}
