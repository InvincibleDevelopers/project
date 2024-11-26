package invincibleDevs.bookpago.readingClub.repository;

import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReadingClubRepository extends JpaRepository<ReadingClub, Long>,
        ReadingClubCustomRepository {

//    @Query("SELECT rc FROM ReadingClub rc")
//    Page<ReadingClub> findAll(Pageable pageable);

    @Query(value = "SELECT rc FROM ReadingClub rc JOIN FETCH rc.weekDay order by rc.id desc",
            countQuery = "SELECT COUNT(rc) FROM ReadingClub rc")
    Page<ReadingClub> findAllWithWeekDays(Pageable pageable);

}
