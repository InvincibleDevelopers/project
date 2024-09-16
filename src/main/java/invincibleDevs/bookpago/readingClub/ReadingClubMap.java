package invincibleDevs.bookpago.readingClub;

import invincibleDevs.bookpago.profile.model.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReadingClubMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Profile clubAdmin;

    @ManyToOne
    private Profile clubMember;

    @ManyToOne
    private ReadingClub readingClub;

}
