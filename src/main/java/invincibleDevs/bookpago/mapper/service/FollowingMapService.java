package invincibleDevs.bookpago.mapper.service;

import invincibleDevs.bookpago.mapper.model.FollowTable;
import invincibleDevs.bookpago.mapper.repository.FollowingMapRepository;
import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.profile.repository.ProfileRepository;
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
    private final ProfileRepository profileRepository;

    @Transactional
    public boolean savefollowingMap(Map<Profile, Profile> followingMap) { //토클 기능
        FollowTable thisFollowTable = FollowTable.builder()
                .build();

        for (Map.Entry<Profile, Profile> entry : followingMap.entrySet()) {
            Profile follower = entry.getKey();   // 팔로워 프로필
            Profile followee = entry.getValue(); // 팔로위 프로필
            thisFollowTable = thisFollowTable.toBuilder()
                    .follower(follower)
                    .followee(followee)
                    .build();
        }
        // 팔로워와 팔로위의 존재 여부를 확인하고 저장
        if (!followingMapRepository.existsByFollowerAndFollowee(thisFollowTable.getFollower(),
                thisFollowTable.getFollowee())) {
            followingMapRepository.save(thisFollowTable);
            return true;
        } else {
            followingMapRepository.deleteByFollowerAndFollowee(thisFollowTable.getFollower(),
                    thisFollowTable.getFollowee());
            return false;
        }
    }

    public Page<FollowingListDto> getFollowers(Long targetId, int page,
            int size) { //맵에서 팔로이 가 타켓유저인거
        Pageable pageable = PageRequest.of(page, size);
        Page<Profile> followerProfiles = followingMapRepository.findFollowersByProfileId(targetId,
                pageable);
        System.out.println("#######################################");
//        System.out.println(followerProfiles.getContent().get(0).getUserEntity().getUsername());

        // Page에서 List를 가져와서 Stream으로 처리
        List<FollowingListDto> followingListDtos = followerProfiles.getContent().stream()
                .map(profile -> new FollowingListDto(profile.getNickName(),
                        profile.getUserEntity().getUsername()))
                .collect(Collectors.toList());

        // 새로운 Page 객체로 변환하여 반환
        return new PageImpl<>(followingListDtos, pageable, followerProfiles.getTotalElements());
    }
}