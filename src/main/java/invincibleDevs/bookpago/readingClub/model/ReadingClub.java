package invincibleDevs.bookpago.readingClub.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
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
    private String time;
    private int repeatCycle;
    private int memberCount;

    @ElementCollection
    private List<Integer> weekDay;


    @OneToMany(mappedBy = "readingClub", cascade = CascadeType.REMOVE)
    private Set<ReadingClubMembers> clubMembers; // 독서 모임의 회원들 -> 이거갯수세면 몇명참여중 표시

    public void addMember() {
        memberCount++;
    }

    public void removeMember() {
        memberCount--;
    }
}
