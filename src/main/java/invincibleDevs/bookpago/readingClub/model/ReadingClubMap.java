package invincibleDevs.bookpago.readingClub.model;

import invincibleDevs.bookpago.profile.model.Profile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
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
    private Profile clubApplicant;

    @ManyToOne
    private ReadingClub readingClub;

}
