package invincibleDevs.bookpago.profile.request;

public record FollowRequest(
        Long followerKakaoId,
        Long followeeKakaoId
) {

}
