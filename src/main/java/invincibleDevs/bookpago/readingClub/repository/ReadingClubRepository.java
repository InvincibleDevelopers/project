package invincibleDevs.bookpago.readingClub.repository;

import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReadingClubRepository extends JpaRepository<ReadingClub, Long> {

    @Query("SELECT rc FROM ReadingClub rc")
    Page<ReadingClub> findAll(Pageable pageable);
}
