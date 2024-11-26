package invincibleDevs.bookpago.readingClub;

import invincibleDevs.bookpago.common.Direction;
import invincibleDevs.bookpago.common.GeometryUtil;
import invincibleDevs.bookpago.common.Location;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;

public class ReadingClubDistance {

    // 탐색 거리 (km 단위)
    private static final double SEARCH_DISTANCE = 1000;

    /**
     * 기준 지점에서 최대 반경이 되는 북동쪽 좌표를 반환합니다.
     */
    public static Location aroundUserNortheastDot(Location userLocation) {

        double nowLatitude = userLocation.getLatitude(); // 현재 위도 = y 좌표
        double nowLongitude = userLocation.getLongitude(); // 현재 경도 = x 좌표

        return GeometryUtil.calculateByDirection(nowLatitude, nowLongitude, SEARCH_DISTANCE,
                Direction.NORTHEAST.getBearing());
    }

    /**
     * 기준 지점에서 최대 거리가 되는 남서쪽 좌표를 반환합니다.
     */
    public static Location aroundUserSouthwestDot(Location userLocation) {

        double nowLatitude = userLocation.getLatitude(); // 현재 위도 = y 좌표
        double nowLongitude = userLocation.getLongitude(); // 현재 경도 = x 좌표

        return GeometryUtil.calculateByDirection(nowLatitude, nowLongitude, SEARCH_DISTANCE,
                Direction.SOUTHWEST.getBearing());
    }

    public static double calculateDistance(Location userLocation, ReadingClub readingClub) {

        double nowLatitude = userLocation.getLatitude(); // 현재 위도 = y 좌표
        double nowLongitude = userLocation.getLongitude(); // 현재 경도 = x 좌표

        double storeLatitude = readingClub.getLocation().getY(); // 현재 위도 = y 좌표
        double storeLongitude = readingClub.getLocation().getX(); // 현재 경도 = x 좌표

        return GeometryUtil.calculateDistance(nowLatitude, nowLongitude, storeLatitude,
                storeLongitude);
    }

}
