package invincibleDevs.bookpago.profile;

import invincibleDevs.bookpago.common.S3Service;
import invincibleDevs.bookpago.common.exception.CustomException;
import invincibleDevs.bookpago.profile.request.ProfileRequest;
import invincibleDevs.bookpago.profile.request.UpdateProfileRequest;
import invincibleDevs.bookpago.profile.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfileFacade {
    private final ProfileService profileService;
    private final S3Service s3Service;


    public ProfileResponse getProfile(ProfileRequest profileRequest) {

        return profileService.getProfile(profileRequest);
    }

    public ProfileResponse updateProfileImage(MultipartFile file) {
        try{
            String url = s3Service.uploadFile(file);
            deleteProfileImage(url);
            return profileService.updateProfileImage(url);
        }catch (IOException e) {
            throw new CustomException("파일 형식 불일치",e);
        }
    }
    private void deleteProfileImage(String targetUrl){
        s3Service.deleteFile(targetUrl);
    }

    public ProfileResponse updateNickname(UpdateProfileRequest updateProfileRequest) {
            return profileService.updateNickname(updateProfileRequest);
    }

    public ProfileResponse updateIntroduce(UpdateProfileRequest updateProfileRequest) {
        return profileService.updateIntroduce(updateProfileRequest);
    }
}
