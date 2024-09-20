package invincibleDevs.bookpago.profile.facade;

import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.Users.service.UserEntityService;
import invincibleDevs.bookpago.common.S3Service;
import invincibleDevs.bookpago.common.exception.CustomException;
import invincibleDevs.bookpago.profile.MyBookDto;
import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.profile.request.FollowRequest;
import invincibleDevs.bookpago.profile.response.FollowingListDto;
import invincibleDevs.bookpago.mapper.service.FollowingMapService;
import invincibleDevs.bookpago.profile.service.ProfileService;
import invincibleDevs.bookpago.profile.request.ProfileRequest;
import invincibleDevs.bookpago.profile.request.UpdateProfileRequest;
import invincibleDevs.bookpago.profile.response.ProfileResponse;
import invincibleDevs.bookpago.readingClub.service.ReadingClubMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileFacade {
    private final ProfileService profileService;
    private final UserEntityService userEntityService;
    private final ReadingClubMapService readingClubMapService;
    private final FollowingMapService followingMapService;
    private final S3Service s3Service;


    public ProfileResponse getProfile(ProfileRequest profileRequest, String username,int page, int size) {
        Profile profile = profileService.getProfile(profileRequest);
        boolean isMine = profileService.isMyProfile(profileRequest,profile);

        return new ProfileResponse(isMine, profile.getNickName(), profile.getIntroduce(), profile.getProfileImgUrl(), profile.getWishIsbnList() ,Optional.ofNullable(readingClubMapService.getUserClubs(profile,page,size)));
    }

    public ProfileResponse updateProfileImage(MultipartFile file,UpdateProfileRequest updateProfileRequest ) {
        try{
            String url = s3Service.uploadFile(file);
            deleteProfileImage(url);
            return profileService.updateProfileImage(url,updateProfileRequest);
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

    public boolean updateFollow(FollowRequest followRequest) {
        return followingMapService.savefollowingMap(profileService.setFollowingMap(followRequest));
    }

    public Page<FollowingListDto> getFollowers(Long targetId, int page, int size) {
        // ProfileService의 메서드를 호출하여 결과 반환
        return followingMapService.getFollowers(targetId, page, size); // 올바른 서비스 인스턴스를 사용
    }
    public Page<FollowingListDto> getFollowees(Long targetId, int page, int size) {
        // ProfileService의 메서드를 호출하여 결과 반환
        return followingMapService.getFollowers(targetId, page, size); // 올바른 서비스 인스턴스를 사용
    }

//    public Page<MyBookDto> getMyBooks(String username, int page, int size) {
//        UserEntity userEntity = userEntityService.findByUserName(username);
//        return profileService.getMyBooks(userEntity, page, size); // 올바른 서비스 인스턴스를 사용
//    }

}
