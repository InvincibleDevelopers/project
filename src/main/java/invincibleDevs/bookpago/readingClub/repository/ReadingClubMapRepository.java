package invincibleDevs.bookpago.readingClub.repository;

import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMap;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadingClubMapRepository extends JpaRepository<ReadingClubMap, Long> {

    @Query("SELECT DISTINCT rc.readingClub.id FROM ReadingClubMap rc WHERE rc.clubAdmin = :profile OR rc.clubMember = :profile")
    List<Long> findDistinctReadingClubIdsByClubAdminOrClubMember(@Param("profile") Profile profile);

    // 특정 독서 모임(clubId)에서 특정 프로필이 clubAdmin으로 있는지 확인
    @Query("SELECT rc FROM ReadingClubMap rc WHERE rc.readingClub.id = :clubId AND rc.clubAdmin = :profile")
    Optional<ReadingClubMap> findByIdAndAdmin(@Param("clubId") Long clubId,
            @Param("profile") Profile profile);

    // 특정 독서 모임(clubId)에서 특정 프로필이 clubMember로 있는지 확인
    @Query("SELECT rc FROM ReadingClubMap rc WHERE rc.readingClub.id = :clubId AND rc.clubMember = :profile")
    Optional<ReadingClubMap> findByIdAndMember(@Param("clubId") Long clubId,
            @Param("profile") Profile profile);

    // 특정 독서 모임(clubId)에서 특정 프로필이 clubApplicant로 있는지 확인
    @Query("SELECT rc FROM ReadingClubMap rc WHERE rc.readingClub.id = :clubId AND rc.clubApplicant = :profile")
    Optional<ReadingClubMap> findByIdAndApplicant(@Param("clubId") Long clubId,
            @Param("profile") Profile profile);
}
