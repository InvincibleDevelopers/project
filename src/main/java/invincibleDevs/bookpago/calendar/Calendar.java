//package invincibleDevs.bookpago.calendar;
//
//import invincibleDevs.bookpago.profile.Profile;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Entity
//@Getter
//@Builder(toBuilder = true)  // toBuilder = true 설정
//@NoArgsConstructor // 기본 생성자 추가
//@AllArgsConstructor // 모든 필드를 포함한 생성자 추가
//public class Calendar {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne(mappedBy = "calendar", cascade = CascadeType.REMOVE)
//    private Profile profile;
//
//    @OneToMany(mappedBy = "calender", cascade = CascadeType.REMOVE)
//    private List<ReadingLog> readingLogs;
//}
