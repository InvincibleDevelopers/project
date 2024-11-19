package invincibleDevs.bookpago.mapper.service;

import invincibleDevs.bookpago.mapper.model.FollowingMap;
import invincibleDevs.bookpago.mapper.repository.FollowingMapRepository;
import invincibleDevs.bookpago.profile.Profile;
import invincibleDevs.bookpago.profile.response.FollowingListDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // 팔로워와 팔로잉의 존재 여부를 확인하고 저장
        if (!followingMapRepository.existsByFollowerAndFollowee(thisFollowingMap.getFollower(),
                thisFollowingMap.getFollowee())) {
            followingMapRepository.save(thisFollowingMap);
            return true;
        } else {
            followingMapRepository.deleteByFollowerAndFollowee(thisFollowingMap.getFollower(),
                    thisFollowingMap.getFollowee());
            return false;
        }
    }

    public Page<FollowingListDto> getFollowers(Long profileId, int page,
            int size) { //맵에서 팔로이 가 타켓유저인거
        Pageable pageable = PageRequest.of(page, size);
        Page<Profile> followerProfiles = followingMapRepository.findFollowersByProfileId(profileId,
                pageable);

        // Page에서 List를 가져와서 Stream으로 처리
        List<FollowingListDto> followingListDtos = followerProfiles.getContent().stream()
                                                                   .map(profile -> new FollowingListDto(
                                                                           profile.getProfileImgUrl(),
                                                                           profile.getNickName(),
                                                                           profile.getUserEntity()
                                                                                  .getKakaoId()))
                                                                   .collect(Collectors.toList());

        // 새로운 Page 객체로 변환하여 반환
        return new PageImpl<>(followingListDtos, pageable, followerProfiles.getTotalElements());
    }

    public Page<FollowingListDto> getFollowings(Long profileId, int page,
            int size) { //맵에서 팔로이 가 타켓유저인거
        Pageable pageable = PageRequest.of(page, size);
        Page<Profile> followingProfiles = followingMapRepository.findFollowingsByProfileId(
                profileId,
                pageable);
        // Page에서 List를 가져와서 Stream으로 처리
        List<FollowingListDto> followingListDtos = followingProfiles.getContent().stream()
                                                                    .map(profile -> new FollowingListDto(
                                                                            profile.getProfileImgUrl(),
                                                                            profile.getNickName(),
                                                                            profile.getUserEntity()
                                                                                   .getKakaoId()))
                                                                    .collect(Collectors.toList());

        // 새로운 Page 객체로 변환하여 반환
        return new PageImpl<>(followingListDtos, pageable, followingProfiles.getTotalElements());
    }

    public boolean existsByFollowerFollowing(Profile follower, Profile following) {
        return followingMapRepository.existsByFollowerAndFollowee(follower, following);
    }
}