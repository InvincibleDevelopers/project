package invincibleDevs.bookpago.profile.response;

public record ProfileResponse(
        boolean isMyProfile,
        String nickname,
        String introduce,
        String imageUrl
) {
}
