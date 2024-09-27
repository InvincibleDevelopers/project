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

    public Profile findProfilebyUsername(UserEntity userEntity) {
//        UserEntity userEntity = userRepository.findByUsername(username);
        Profile profile = profileRepository.findByUserEntityId(userEntity.getId())
                                           .orElseThrow(() -> new NoSuchElementException(
                                                   "Profile with username :"
                                                           + userEntity.getUsername()
                                                           + "- not found"));
        System.out.println(profile.getNickName());
        return profile;
    }

    public boolean isMyProfile(ProfileRequest profileRequest, Profile profile) {
        String username = Utils.getAuthenticatedUsername();

        if (profile.getUserEntity().getUsername().equals(username)) {
            return true;
        } else {
            // 두 사용자 이름이 일치하지 않을 때의 로직
            return false;
        }
    }

    public Profile getProfile(ProfileRequest profileRequest) {
        try {
//            String username = Utils.getAuthenticatedUsername();
            UserEntity userEntity = userRepository.findByUsername(
                    profileRequest.username()); //요청프로필
//            System.out.println(userEntity.getUsername());
            Profile profile = findProfilebyUsername(userEntity);
//            System.out.println(profile.getNickName());
            return profile;
//                    .orElseThrow(() -> new NoSuchElementException("Profile with username :" + username + "- not found"));
        } catch (CustomException e) {
            throw e;
        }
    }

    public ProfileResponse updateProfileImage(String url,
            UpdateProfileRequest updateProfileRequest) {
        try {
//            String username = Utils.getAuthenticatedUsername();
            System.out.println(updateProfileRequest.username());
            UserEntity userEntity = userRepository.findByUsername(updateProfileRequest.username());

            Profile profile = profileRepository.findByUserEntityId(userEntity.getId())
                                               .orElseThrow(() -> new NoSuchElementException(
                                                       "Profile with username :"
                                                               + userEntity.getUsername()
                                                               + "- not found"));

            Profile updateProfile = profile.toBuilder()
                                           .profileImgUrl(url)
                                           .build();

            profileRepository.save(updateProfile);
            return new ProfileResponse(true, updateProfile.getNickName(),
                    updateProfile.getIntroduce(), updateProfile.getProfileImgUrl(),
                    updateProfile.getWishIsbnList(), null);
        } catch (CustomException e) {
            return new ProfileResponse(false, "Error: " + e.getMessage(), null, null, null, null);
        }
    }

    public ProfileResponse updateNickname(UpdateProfileRequest updateProfileRequest) {
        try {
//            String username = Utils.getAuthenticatedUsername();
//            UserEntity userEntity = userRepository.findByUsername(username);
            UserEntity userEntity = userRepository.findByUsername(updateProfileRequest.username());

            Profile profile = profileRepository.findByUserEntityId(userEntity.getId())
                                               .orElseThrow(() -> new NoSuchElementException(
                                                       "Profile with username :"
                                                               + updateProfileRequest.username()
                                                               + "- not found"));
            Profile updateProfile = profile.toBuilder()
                                           .nickName(updateProfileRequest.nickname().
                                                                         orElseThrow(
                                                                                 () -> new IllegalArgumentException(
                                                                                         "변경값 필수")))
                                           .build();
            profileRepository.save(updateProfile);
            return new ProfileResponse(true, updateProfile.getNickName(),
                    updateProfile.getIntroduce(), updateProfile.getProfileImgUrl(),
                    updateProfile.getWishIsbnList(), null);

        } catch (CustomException e) {
            return new ProfileResponse(false, "Error: " + e.getMessage(), null, null, null, null);
        }
    }

    public ProfileResponse updateIntroduce(UpdateProfileRequest updateProfileRequest) {
        try {
//            String username = Utils.getAuthenticatedUsername();
            UserEntity userEntity = userRepository.findByUsername(updateProfileRequest.username());

            Profile profile = profileRepository.findByUserEntityId(userEntity.getId())
                                               .orElseThrow(() -> new NoSuchElementException(
                                                       "Profile with username :"
                                                               + updateProfileRequest.username()
                                                               + "- not found"));

            Profile updateProfile = profile.toBuilder()
                                           .introduce(updateProfileRequest.introduce().
                                                                          orElseThrow(
                                                                                  () -> new IllegalArgumentException(
                                                                                          "변경값 필수")))
                                           .build();

            profileRepository.save(updateProfile);
            return new ProfileResponse(true, updateProfile.getNickName(),
                    updateProfile.getIntroduce(), updateProfile.getProfileImgUrl(),
                    updateProfile.getWishIsbnList(), null);
        } catch (CustomException e) {
            return new ProfileResponse(false, "Error: " + e.getMessage(), null, null, null, null);
        }
    }

    public Map<Profile, Profile> setFollowingMap(FollowRequest followRequest) {
        try {
            Map<Profile, Profile> followingMap = new HashMap<>();

            // 예시: 팔로우하는 사용자와 팔로우되는 사용자 프로필을 생성합니다.
//            UserEntity userEntity = userRepository.findByUsername(followRequest.follower());
//            UserEntity targetUserEntity = userRepository.findByUsername(followRequest.followee());
            Profile follower = profileRepository.findByUserEntityUserName(followRequest.follower())
                                                .orElseThrow(() -> new NoSuchElementException(
                                                        "프로필을 찾을 수 없음"));

            Profile followee = profileRepository.findByUserEntityUserName(followRequest.followee())
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

    public String addWishBook(String nickname, Long isbn) {
        Profile profile = profileRepository.findByNickName(nickname);
        if (existsIsbnInWishList(profile.getId(), isbn)) {
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

    public boolean existsIsbnInWishList(Long profileId, Long isbn) {
        Optional<Profile> profileOpt = profileRepository.findById(profileId);

        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            List<Long> wishIsbnList = profile.getWishIsbnList();

            // wishIsbnList에 해당 isbn이 존재하는지 확인
            return wishIsbnList.contains(isbn);
        }

        return false;
    }
}
