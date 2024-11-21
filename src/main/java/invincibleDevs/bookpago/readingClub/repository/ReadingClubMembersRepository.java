package invincibleDevs.bookpago.readingClub.repository;

import invincibleDevs.bookpago.profile.Profile;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMembers;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadingClubMembersRepository extends JpaRepository<ReadingClubMembers, Long> {

    // 특정 프로필이 소속된 독서 모임의 고유 ID 목록을 조회
    @Query("SELECT DISTINCT rc.readingClub.id FROM ReadingClubMembers rc WHERE rc.clubMember.id = :profileId")
    List<Long> findDistinctReadingClubIdsByClubAdminOrClubMember(
            @Param("profileId") Long profileId);

    // 특정 독서 모임(clubId)에서 특정 프로필이 clubAdmin으로 있는지 확인
    @Query("SELECT rc FROM ReadingClubMembers rc WHERE rc.readingClub.id = :clubId AND rc.clubMember = :profile AND rc.isAdmin = true")
    Optional<ReadingClubMembers> findByIdAndAdmin(@Param("clubId") Long clubId,
            @Param("profile") Profile profile);

    // 특정 독서 모임(clubId)에서 특정 프로필이 clubMember로 있는지 확인
    @Query("SELECT rc FROM ReadingClubMembers rc WHERE rc.readingClub.id = :clubId AND rc.clubMember = :profile")
    Optional<ReadingClubMembers> findByIdAndMember(@Param("clubId") Long clubId,
            @Param("profile") Profile profile);

    // 특정 독서 모임(clubId)에서 특정 프로필이 clubApplicant로 있는지 확인
//    @Query("SELECT rc FROM ReadingClubMembers rc WHERE rc.readingClub.id = :clubId AND rc.clubApplicant = :profile")
//    Optional<ReadingClubMembers> findByIdAndApplicant(@Param("clubId") Long clubId,
//            @Param("profile") Profile profile);

    // 특정 독서 모임(clubId)에서 멤버 리스트 조회
    @Query("SELECT rc FROM ReadingClubMembers rc WHERE rc.readingClub.id = :clubId AND rc.clubMember IS NOT NULL")
    List<ReadingClubMembers> findMembersByClubId(@Param("clubId") Long clubId);

//    // 특정 독서 모임(clubId)에서 대기자 리스트 조회
//    @Query("SELECT rc FROM ReadingClubMembers rc WHERE rc.readingClub.id = :clubId AND rc.clubApplicant IS NOT NULL")
//    List<ReadingClubMembers> findApplicantsByClubId(@Param("clubId") Long clubId);

    // 특정 독서 모임(clubId)에서 관리자 조회
    @Query("SELECT rc FROM ReadingClubMembers rc WHERE rc.readingClub.id = :clubId AND rc.isAdmin = true")
    Optional<ReadingClubMembers> findAdminByClubId(@Param("clubId") Long clubId);

    // 특정 독서 모임(clubId)에서 전체 멤버 조회
    @Query("SELECT rc FROM ReadingClubMembers rc WHERE rc.readingClub.id = :clubId")
    Optional<List<ReadingClubMembers>> findAllByClubId(@Param("clubId") Long clubId);

    // 특정 클럽 ID와 멤버 ID로 조회
    Optional<ReadingClubMembers> findByClubMember_IdAndReadingClub_Id(Long clubId,
            Long clubMemberId);

}
