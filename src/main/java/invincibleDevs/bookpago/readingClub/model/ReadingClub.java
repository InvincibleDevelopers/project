package invincibleDevs.bookpago.readingClub.model;

import invincibleDevs.bookpago.readingClub.model.ReadingClubMap;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

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

    @OneToMany(mappedBy = "readingClub",cascade = CascadeType.REMOVE)
    private Set<ReadingClubMap> clubMembers; // 독서 모임의 회원들

}
