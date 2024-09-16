package invincibleDevs.bookpago.profile.service;

import invincibleDevs.bookpago.profile.model.FollowingMap;
import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.profile.repository.FollowingMapRepository;
import invincibleDevs.bookpago.profile.response.FollowingListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.stream.Collectors;



import java.util.List;
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

    public Page<FollowingListDto> getFollowers(String username, int page) {
        Pageable pageable = PageRequest.of(page,50);
        Page<Profile> followerProfiles = followingMapRepository.findFollowersByFollowee(username,pageable);
        System.out.println("#######################################");
        System.out.println(followerProfiles.getSize());


        // Page에서 List를 가져와서 Stream으로 처리
        List<FollowingListDto> followingListDtos = followerProfiles.getContent().stream()
                .map(profile -> new FollowingListDto(profile.getProfileImgUrl(), profile.getNickName()))
                .collect(Collectors.toList());

        // 새로운 Page 객체로 변환하여 반환
        return new PageImpl<>(followingListDtos, pageable, followerProfiles.getTotalElements());
    }
}