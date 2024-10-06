package invincibleDevs.bookpago.readingClub.repository;

import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReadingClubMapRepository extends JpaRepository<ReadingClubMap,Long> {
    // 특정 Profile이 clubAdmin인 ReadingClubMap 엔티티 찾기
    @Query("SELECT rc FROM ReadingClubMap rc WHERE rc.clubAdmin = :profile")
    List<ReadingClubMap> findByClubAdmin(@Param("profile") Profile profile);

    // 특정 Profile이 clubMember인 ReadingClubMap 엔티티 찾기
    @Query("SELECT rc FROM ReadingClubMap rc WHERE rc.clubMember = :profile")
    List<ReadingClubMap> findByClubMember(@Param("profile") Profile profile);

    // 특정 독서 모임(clubId)에서 특정 프로필이 clubAdmin으로 있는지 확인
    @Query("SELECT rc FROM ReadingClubMap rc WHERE rc.readingClub.id = :clubId AND rc.clubAdmin = :profile")
    Optional<ReadingClubMap> findByIdAndAdmin(@Param("clubId") Long clubId, @Param("profile") Profile profile);

    // 특정 독서 모임(clubId)에서 특정 프로필이 clubMember로 있는지 확인
    @Query("SELECT rc FROM ReadingClubMap rc WHERE rc.readingClub.id = :clubId AND rc.clubMember = :profile")
    Optional<ReadingClubMap> findByIdAndMember(@Param("clubId") Long clubId, @Param("profile") Profile profile);

    // 특정 독서 모임(clubId)에서 특정 프로필이 clubApplicant로 있는지 확인
    @Query("SELECT rc FROM ReadingClubMap rc WHERE rc.readingClub.id = :clubId AND rc.clubApplicant = :profile")
    Optional<ReadingClubMap> findByIdAndApplicant(@Param("clubId") Long clubId, @Param("profile") Profile profile);

    // 특정 독서 모임(clubId)에서 멤버 리스트 조회
    @Query("SELECT rc FROM ReadingClubMap rc WHERE rc.readingClub.id = :clubId AND rc.clubMember IS NOT NULL")
    List<ReadingClubMap> findMembersByClubId(@Param("clubId") Long clubId);

    // 특정 독서 모임(clubId)에서 대기자 리스트 조회
    @Query("SELECT rc FROM ReadingClubMap rc WHERE rc.readingClub.id = :clubId AND rc.clubApplicant IS NOT NULL")
    List<ReadingClubMap> findApplicantsByClubId(@Param("clubId") Long clubId);

    // 특정 독서 모임(clubId)에서 관리자 조회
    @Query("SELECT rc FROM ReadingClubMap rc WHERE rc.readingClub.id = :clubId AND rc.clubAdmin IS NOT NULL")
    Optional<ReadingClubMap> findAdminByClubId(@Param("clubId") Long clubId);
}
