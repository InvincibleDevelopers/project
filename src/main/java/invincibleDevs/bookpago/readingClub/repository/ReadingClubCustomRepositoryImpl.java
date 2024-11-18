package invincibleDevs.bookpago.readingClub.repository;

import invincibleDevs.bookpago.common.Location;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTWriter;

public class ReadingClubCustomRepositoryImpl implements ReadingClubCustomRepository {

    private final EntityManager em;

    public ReadingClubCustomRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<ReadingClub> findByLocation(Location northeast, Location southwest) {

        GeometryFactory geometryFactory = new GeometryFactory();
        
        // POLYGON 생성 (사각형)
        Coordinate[] coordinates = new Coordinate[]{
                new Coordinate(northeast.getLatitude(), northeast.getLongitude()), // Top-right
                new Coordinate(southwest.getLatitude(), northeast.getLongitude()), // Top-left
                new Coordinate(southwest.getLatitude(), southwest.getLongitude()), // Bottom-left
                new Coordinate(northeast.getLatitude(), southwest.getLongitude()), // Bottom-right
                new Coordinate(northeast.getLatitude(), northeast.getLongitude())
                // Close the polygon
        };
        Polygon polygon = geometryFactory.createPolygon(coordinates);
        polygon.setSRID(4326);
        // POLYGON을 WKT 문자열로 변환
        WKTWriter wktWriter = new WKTWriter();
        String wktPolygon = wktWriter.write(polygon);
        System.out.println("Generated Polygon WKT: " + wktPolygon);

        System.out.println("Generated Polygon WKT: " + polygon.toText());

        // 네이티브 쿼리 실행
        Query nativeQuery = em.createNativeQuery(
                "SELECT * FROM reading_club WHERE ST_Within(location, ST_GeomFromText(:polygon, 4326))",
                ReadingClub.class
        );
        nativeQuery.setParameter("polygon", wktPolygon);
        nativeQuery.setMaxResults(10);

        // 결과 가져오기
        List<ReadingClub> results = nativeQuery.getResultList();
        for (ReadingClub readingClub : results) {
            System.out.println(readingClub);
        }

        return results;


    }
}
