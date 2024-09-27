package invincibleDevs.bookpago.readingClub.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 포함한 생성자 추가
public class ReadingClub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clubName;
    private String location;
    private String description;
    private String meetingTime;

    @OneToMany(mappedBy = "readingClub", cascade = CascadeType.REMOVE)
    private Set<ReadingClubMap> clubMembers; // 독서 모임의 회원들 -> 이거갯수세면 몇명참여중 표시

}
