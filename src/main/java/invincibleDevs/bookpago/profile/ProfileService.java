package invincibleDevs.bookpago.profile;

import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.Users.repository.UserRepository;
import invincibleDevs.bookpago.common.Utils;
import invincibleDevs.bookpago.common.exception.CustomException;
import invincibleDevs.bookpago.profile.request.ProfileRequest;
import invincibleDevs.bookpago.profile.request.UpdateProfileRequest;
import invincibleDevs.bookpago.profile.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileResponse getProfile(ProfileRequest profileRequest) {
        try{
            String username = Utils.getAuthenticatedUsername();
            UserEntity userEntity = userRepository.findByUsername(username);

            Profile profile = profileRepository.findByUserEntityId(userEntity.getId())
                    .orElseThrow(() -> new NoSuchElementException("Profile with username :" + username + "- not found"));
            if (profile.getUserEntity().getUsername().equals(username)) {
                return new ProfileResponse(true, profile.getNickName(), profile.getIntroduce(), profile.getProfileImgUrl());
            } else {
                // 두 사용자 이름이 일치하지 않을 때의 로직
                return new ProfileResponse(false, profile.getNickName(), profile.getIntroduce(), profile.getProfileImgUrl());
            }
        }catch (CustomException e) {
            return new ProfileResponse(false, "Error: " + e.getMessage(), null, null);
        }
    }

    public ProfileResponse updateProfileImage(String url) {
        try{
            String username = Utils.getAuthenticatedUsername();
            UserEntity userEntity = userRepository.findByUsername(username);

            Profile profile = profileRepository.findByUserEntityId(userEntity.getId())
                    .orElseThrow(() -> new NoSuchElementException("Profile with username :" + username + "- not found"));

            Profile updateProfile = profile.toBuilder()
                    .profileImgUrl(url)
                    .build();

            profileRepository.save(updateProfile);
            return new ProfileResponse(true, updateProfile.getNickName(), updateProfile.getIntroduce(), updateProfile.getProfileImgUrl());
        } catch (CustomException e) {
            return new ProfileResponse(false, "Error: " + e.getMessage(), null, null);
        }
    }

    public ProfileResponse updateNickname(UpdateProfileRequest updateProfileRequest) {
        try{
            String username = Utils.getAuthenticatedUsername();
            UserEntity userEntity = userRepository.findByUsername(username);

            Profile profile = profileRepository.findByUserEntityId(userEntity.getId())
                    .orElseThrow(() -> new NoSuchElementException("Profile with username :" + username + "- not found"));
            Profile updateProfile = profile.toBuilder()
                    .nickName(updateProfileRequest.nickname().
                            orElseThrow(()-> new IllegalArgumentException("변경값 필수")) )
                    .build();
            profileRepository.save(updateProfile);
            return new ProfileResponse(true, updateProfile.getNickName(), updateProfile.getIntroduce(),updateProfile.getProfileImgUrl());

        } catch(CustomException e) {
            return new ProfileResponse(false, "Error: " + e.getMessage(), null, null);
        }
    }

    public ProfileResponse updateIntroduce(UpdateProfileRequest updateProfileRequest) {
        try{
            String username = Utils.getAuthenticatedUsername();
            UserEntity userEntity = userRepository.findByUsername(username);

            Profile profile = profileRepository.findByUserEntityId(userEntity.getId())
                    .orElseThrow(() -> new NoSuchElementException("Profile with username :" + username + "- not found"));

            Profile updateProfile = profile.toBuilder()
                    .nickName(updateProfileRequest.introduce().
                            orElseThrow(()-> new IllegalArgumentException("변경값 필수")) )
                    .build();

            profileRepository.save(updateProfile);
            return new ProfileResponse(true, updateProfile.getNickName(), updateProfile.getIntroduce(),updateProfile.getProfileImgUrl());
        }catch (CustomException e) {
            return new ProfileResponse(false, "Error: " + e.getMessage(), null, null);
        }
    }
}
