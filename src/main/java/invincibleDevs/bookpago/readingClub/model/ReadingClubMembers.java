package invincibleDevs.bookpago.readingClub.model;

import invincibleDevs.bookpago.profile.model.Profile;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReadingClubMembers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    private Profile clubAdmin;

    @ManyToOne
    private Profile clubMember;

//    @ManyToOne
//    private Profile clubApplicant;

    @ManyToOne
    private ReadingClub readingClub;

    private boolean isAdmin = false;

}
