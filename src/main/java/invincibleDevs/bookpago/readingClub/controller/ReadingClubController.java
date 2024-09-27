package invincibleDevs.bookpago.readingClub.controller;

import invincibleDevs.bookpago.readingClub.dto.ReadingClubRequest;
import invincibleDevs.bookpago.readingClub.service.ReadingClubFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            return ResponseEntity.ok(readingClubFacade.createClub(readingClubRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/clubs/{clubId}")
    public ResponseEntity<?> getTargetClub(
            @PathVariable("clubId") Long clubId,
            @RequestParam(value = "nickname") String nickname
    ) {
        return ResponseEntity.ok(readingClubFacade.getClub(nickname, clubId));
    }

}
