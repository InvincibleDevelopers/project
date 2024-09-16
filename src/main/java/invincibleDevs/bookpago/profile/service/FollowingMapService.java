package invincibleDevs.bookpago.profile.service;

import invincibleDevs.bookpago.profile.model.FollowingMap;
import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.profile.repository.FollowingMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FollowingMapService {
    private final FollowingMapRepository followingMapRepository;

    @Transactional
    public boolean savefollowingMap(Map<Profile, Profile> followingMap) { //토클 기능
        FollowingMap thisFollowingMap = FollowingMap.builder()
                .build();

        for (Map.Entry<Profile, Profile> entry : followingMap.entrySet()) {
            Profile follower = entry.getKey();   // 팔로워 프로필
            Profile followee = entry.getValue(); // 팔로위 프로필
            thisFollowingMap = thisFollowingMap.toBuilder()
                    .follower(follower)
                    .followee(followee)
                    .build();
        }
        // 팔로워와 팔로위의 존재 여부를 확인하고 저장
        if (!followingMapRepository.existsByFollowerAndFollowee(thisFollowingMap.getFollower(), thisFollowingMap.getFollowee())) {
            followingMapRepository.save(thisFollowingMap);
            return true;
        } else {
            followingMapRepository.deleteByFollowerAndFollowee(thisFollowingMap.getFollower(), thisFollowingMap.getFollowee());
            return false;
        }
    }


}