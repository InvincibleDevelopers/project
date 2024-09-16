package invincibleDevs.bookpago.readingClub.repository;

import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReadingClubMapRepository extends JpaRepository<ReadingClubMap,Long> {
    // 특정 Profile이 clubAdmin인 ReadingClubMap 엔티티 찾기
    @Query("SELECT rc FROM ReadingClubMap rc WHERE rc.clubAdmin = :profile")
    List<ReadingClubMap> findByClubAdmin(@Param("profile") Profile profile);

    // 특정 Profile이 clubMember인 ReadingClubMap 엔티티 찾기
    @Query("SELECT rc FROM ReadingClubMap rc WHERE rc.clubMember = :profile")
    List<ReadingClubMap> findByClubMember(@Param("profile") Profile profile);
}
