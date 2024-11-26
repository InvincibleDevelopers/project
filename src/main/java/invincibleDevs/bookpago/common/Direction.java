package invincibleDevs.bookpago.common;

public enum Direction {
    NORTH(0.0),         // 북쪽 0도
    NORTHEAST(45.0),    // 북동쪽 45도
    EAST(90.0),         // 동쪽 90도
    SOUTHEAST(135.0),   // 남동쪽 135도
    SOUTH(180.0),       // 남쪽 180도
    SOUTHWEST(225.0),   // 남서쪽 225도
    WEST(270.0),        // 서쪽 270도
    NORTHWEST(315.0);   // 북서쪽 315도

    private final double bearing; // 방위각 (Bearing)

    Direction(double bearing) {
        this.bearing = bearing;
    }

    // 방위각 반환
    public double getBearing() {
        return this.bearing;
    }
}

