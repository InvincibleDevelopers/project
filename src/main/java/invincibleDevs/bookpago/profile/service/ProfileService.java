package invincibleDevs.bookpago.profile.service;

import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.Users.repository.UserRepository;
import invincibleDevs.bookpago.common.Utils;
import invincibleDevs.bookpago.common.exception.CustomException;
import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.profile.repository.ProfileRepository;
import invincibleDevs.bookpago.profile.request.FollowRequest;
import invincibleDevs.bookpago.profile.request.ProfileRequest;
import invincibleDevs.bookpago.profile.request.UpdateProfileRequest;
import invincibleDevs.bookpago.profile.response.ProfileResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public Profile findProfilebyUser(UserEntity userEntity) {
        Profile profile = profileRepository.findByUserEntityId(userEntity.getId())
                                           .orElseThrow(() -> new NoSuchElementException(
                                                   "Profile with username :"
                                                           + userEntity.getKakaoId()
                                                           + "- not found"));
        System.out.println(profile.getNickName());
        return profile;
    }

    public boolean isMyProfile(ProfileRequest profileRequest, Profile profile) {
        String username = Utils.getAuthenticatedUsername();

        if (profile.getUserEntity().getKakaoId().equals(username)) {
            return true;
        } else {
            // 두 사용자 이름이 일치하지 않을 때의 로직
            return false;
        }
    }

    public Profile getProfile(ProfileRequest profileRequest) {
        try {
            UserEntity userEntity = userRepository.findByKakaoId(
                    profileRequest.kakaoId()); //요청프로필
            Profile profile = findProfilebyUser(userEntity);
            return profile;
        } catch (CustomException e) {
            throw e;
        }
    }

    public ProfileResponse updateProfileImage(String url,
            UpdateProfileRequest updateProfileRequest) {
        try {
            UserEntity userEntity = userRepository.findByKakaoId(updateProfileRequest.kakaoId());

            Profile profile = profileRepository.findByUserEntityId(userEntity.getId())
                                               .orElseThrow(() -> new NoSuchElementException(
                                                       "Profile with username :"
                                                               + userEntity.getKakaoId()
                                                               + "- not found"));

            Profile updateProfile = profile.toBuilder()
                                           .profileImgUrl(url)
                                           .build();

            profileRepository.save(updateProfile);
            return new ProfileResponse(updateProfileRequest.kakaoId(), updateProfile.getNickName(),
                    updateProfile.getIntroduce(), updateProfile.getProfileImgUrl(),
                    updateProfile.getWishIsbnList(), null);
        } catch (CustomException e) {
            return new ProfileResponse(null, "Error: " + e.getMessage(), null, null, null, null);
        }
    }

    public ProfileResponse updateNickname(UpdateProfileRequest updateProfileRequest) {
        try {
            UserEntity userEntity = userRepository.findByKakaoId(updateProfileRequest.kakaoId());

            Profile profile = profileRepository.findByUserEntityId(userEntity.getId())
                                               .orElseThrow(() -> new NoSuchElementException(
                                                       "Profile with username :"
                                                               + updateProfileRequest.kakaoId()
                                                               + "- not found"));
            Profile updateProfile = profile.toBuilder()
                                           .nickName(updateProfileRequest.nickname().
                                                                         orElseThrow(
                                                                                 () -> new IllegalArgumentException(
                                                                                         "변경값 필수")))
                                           .build();
            profileRepository.save(updateProfile);
            return new ProfileResponse(updateProfile.getUserEntity().getKakaoId(),
                    updateProfile.getNickName(),
                    updateProfile.getIntroduce(), updateProfile.getProfileImgUrl(),
                    updateProfile.getWishIsbnList(), null);

        } catch (CustomException e) {
            return new ProfileResponse(null, "Error: " + e.getMessage(), null, null, null, null);
        }
    }

    public ProfileResponse updateIntroduce(UpdateProfileRequest updateProfileRequest) {
        try {
//            String username = Utils.getAuthenticatedUsername();
            UserEntity userEntity = userRepository.findByKakaoId(updateProfileRequest.kakaoId());

            Profile profile = profileRepository.findByUserEntityId(userEntity.getId())
                                               .orElseThrow(() -> new NoSuchElementException(
                                                       "Profile with username :"
                                                               + updateProfileRequest.kakaoId()
                                                               + "- not found"));

            Profile updateProfile = profile.toBuilder()
                                           .introduce(updateProfileRequest.introduce().
                                                                          orElseThrow(
                                                                                  () -> new IllegalArgumentException(
                                                                                          "변경값 필수")))
                                           .build();

            profileRepository.save(updateProfile);
            return new ProfileResponse(updateProfile.getUserEntity().getKakaoId(),
                    updateProfile.getNickName(),
                    updateProfile.getIntroduce(), updateProfile.getProfileImgUrl(),
                    updateProfile.getWishIsbnList(), null);
        } catch (CustomException e) {
            return new ProfileResponse(null, "Error: " + e.getMessage(), null, null, null, null);
        }
    }

    public Map<Profile, Profile> setFollowingMap(FollowRequest followRequest) {
        try {
            Map<Profile, Profile> followingMap = new HashMap<>();

            // 예시: 팔로우하는 사용자와 팔로우되는 사용자 프로필을 생성합니다.
//            UserEntity userEntity = userRepository.findByUsername(followRequest.follower());
//            UserEntity targetUserEntity = userRepository.findByUsername(followRequest.followee());
            Profile follower = profileRepository.findByUserEntityUserKakaoId(
                                                        followRequest.followerKakaoId())
                                                .orElseThrow(() -> new NoSuchElementException(
                                                        "프로필을 찾을 수 없음"));

            Profile followee = profileRepository.findByUserEntityUserKakaoId(
                                                        followRequest.followeeKakaoId())
                                                .orElseThrow(() -> new NoSuchElementException(
                                                        "프로필을 찾을 수 없음"));

            followingMap.put(follower, followee);
            return followingMap;

//            String username = Utils.getAuthenticatedUsername();
//            UserEntity userEntity = userRepository.findByUsername(username);

//        followingMapService.savefollowingMap(follower, followee);
        } catch (CustomException e) {
            throw e;
        }

    }


    public Profile findByNickname(String nickname) {
        Profile profile = profileRepository.findByNickName(nickname);
        return profile;

    }

    public String addWishBook(Long kakaoId, Long isbn) {
        Profile profile = profileRepository.findByUserEntityUserKakaoId(kakaoId)
                                           .orElseThrow();
        if (existsIsbnInWishList(profile.getUserEntity().getKakaoId(), isbn)) {
            // wishIsbnList에서 isbn 삭제
            List<Long> wishIsbnList = profile.getWishIsbnList();
            wishIsbnList.remove(isbn);

            // 변경된 wishIsbnList를 가진 Profile 저장
            profileRepository.save(profile);
            return "Remove WishBook";
        }
        List<Long> updateWishBookList = new ArrayList<>(profile.getWishIsbnList());
        updateWishBookList.add(isbn);
        Profile updateProfile = profile.toBuilder()
                                       .wishIsbnList(updateWishBookList)
                                       .build();
        profileRepository.save(updateProfile);
        return "Add WishBook";
    }

    public boolean existsIsbnInWishList(Long kakaoId, Long isbn) {
        Optional<Profile> profileOpt = profileRepository.findByUserEntityUserKakaoId(kakaoId);

        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            List<Long> wishIsbnList = profile.getWishIsbnList();

            // wishIsbnList에 해당 isbn이 존재하는지 확인
            return wishIsbnList.contains(isbn);
        }

        return false;
    }
}
