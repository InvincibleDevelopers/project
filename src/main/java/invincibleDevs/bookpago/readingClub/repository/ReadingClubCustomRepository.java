package invincibleDevs.bookpago.readingClub.repository;

import invincibleDevs.bookpago.common.Location;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import java.util.List;

public interface ReadingClubCustomRepository {

    List<ReadingClub> findByLocation(Location northeast, Location southwest);

    List<ReadingClub> findByLocationOrderByDistance(Location northeast, Location southwest,
            int page, int size, Location location);
}

