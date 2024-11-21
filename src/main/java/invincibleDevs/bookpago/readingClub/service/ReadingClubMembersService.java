package invincibleDevs.bookpago.readingClub.service;

import invincibleDevs.bookpago.common.exception.CustomException;
import invincibleDevs.bookpago.profile.Profile;
import invincibleDevs.bookpago.readingClub.dto.ReadingClubDto;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMembers;
import invincibleDevs.bookpago.readingClub.repository.ReadingClubMembersRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadingClubMembersService {

    private final ReadingClubMembersRepository readingClubMembersRepository;
    private final ReadingClubService readingClubService;

    public Page<ReadingClubDto> getUserClubs(Profile profile, int page, int size) {
        // 관리자와 멤버로 검색하여 리스트 가져오기
        List<Long> clubIds = readingClubMembersRepository.findDistinctReadingClubIdsByClubAdminOrClubMember(
                profile.getId());
        System.out.println("%%%%%%%%%%%%%%");
        System.out.println(clubIds.size());
        // ReadingClubDto 리스트 초기화
        List<ReadingClubDto> readingClubDtoList = new ArrayList<>();
        for (Long clubId : clubIds) {
            ReadingClub readingClub = readingClubService.findById(clubId);
            ReadingClubDto readingClubDto = new ReadingClubDto(
                    readingClub.getId(),
                    readingClub.getClubMembers().size(),
                    readingClub.getClubName(),
                    readingClub.getLocation().getX(),
                    readingClub.getLocation().getY(),
                    readingClub.getDescription(),
                    readingClub.getTime(),
                    readingClub.getRepeatCycle(),
                    readingClub.getWeekDay()
            );
            readingClubDtoList.add(readingClubDto);

        }

        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), readingClubDtoList.size());

        // 유효한 범위 내의 결과 리스트만 추출
        List<ReadingClubDto> content = (start > readingClubDtoList.size())
                ? new ArrayList<>()
                : readingClubDtoList.subList(start, end);

        // 페이징된 결과 리스트 반환
        return new PageImpl<>(content, pageable, readingClubDtoList.size());

    }

    public ReadingClubMembers create_admin(Profile admin, ReadingClub readingClub) {
        ReadingClubMembers readingClubMembers = ReadingClubMembers.builder()
                                                                  .clubMember(admin)
                                                                  .readingClub(readingClub)
                                                                  .isAdmin(true)
                                                                  .build();
        return readingClubMembersRepository.save(readingClubMembers);
    }

//    public ReadingClubMembers create_applicant(Profile applicant, ReadingClub readingClub) {
//        ReadingClubMembers readingClubMembers = ReadingClubMembers.builder()
//                                                                  .clubApplicant(applicant)
//                                                                  .readingClub(readingClub)
//                                                                  .build();
//        return readingClubMapRepository.save(readingClubMembers);
//    }

    public ReadingClubMembers create_member(Profile member, ReadingClub readingClub) {
        ReadingClubMembers readingClubMembers = ReadingClubMembers.builder()
                                                                  .clubMember(member)
                                                                  .readingClub(readingClub)
                                                                  .build();
        return readingClubMembersRepository.save(readingClubMembers);
    }

    public ReadingClubMembers delete(Profile admin, ReadingClub readingClub) {
        ReadingClubMembers readingClubMembers = ReadingClubMembers.builder()
                                                                  .clubMember(admin)
                                                                  .readingClub(readingClub)
                                                                  .build();
        return readingClubMembersRepository.save(readingClubMembers);
    }

    public List<Long> getMemberProfileIdList(Long clubId,
            Long profileId) { //g해당카카오아이디 admin에잇는지;
        List<ReadingClubMembers> memberProfileList = readingClubMembersRepository.findAllByClubId(
                                                                                         clubId)
                                                                                 .orElseThrow(
                                                                                         () -> new CustomException(
                                                                                                 "not found members."));

        List<Long> memberProfileIdList;
        memberProfileIdList = readingClubMembersRepository.findAllByClubId(clubId)
                                                          .orElseThrow(
                                                                  () -> new CustomException(
                                                                          "not found members."))
                                                          .stream()
                                                          .map(readingClubMembers -> readingClubMembers.getClubMember()
                                                                                                       .getId())
                                                          .collect(Collectors.toList());

        return memberProfileIdList;
        // 관리자 정보를 맴버 리스트에 추가
//        Optional<ReadingClubMembers
//                > adminMapOptional = readingClubMembersRepository.findAdminByClubId(
//                clubId);
//        if (adminMapOptional.isPresent()) {
//            Profile adminProfile = adminMapOptional.get().getClubAdmin();
//            ProfileDTO adminDto = new ProfileDTO(
//                    adminProfile.getUserEntity().getKakaoId(),
//                    adminProfile.getNickName(),
//                    adminProfile.getProfileImgUrl()
//            );
//            memberProfiles.add(adminDto);
//        }

        // 맴버 정보를 맴버 리스트에 추가
//        List<ReadingClubMembers> memberMaps = readingClubMembersRepository.findMembersByClubId(
//                clubId);
//        for (ReadingClubMembers memberMap : memberMaps) {
//            Profile memberProfile = memberMap.getClubMember();
//            ProfileDTO memberDto = new ProfileDTO(
//                    memberProfile.getUserEntity().getKakaoId(),
//                    memberProfile.getNickName(),
//                    memberProfile.getProfileImgUrl()
//            );
//            memberProfiles.add(memberDto);
//        }

//        return memberProfiles;
    }

//    public List<ProfileDTO> getApplicantProfiles(Long clubId) {
//        List<ProfileDTO> applicantProfiles = new ArrayList<>();
//
//        // 맴버 정보를 맴버 리스트에 추가
//        List<ReadingClubMembers> applicantMaps = readingClubMembersRepository.findApplicantsByClubId(
//                clubId);
//        for (ReadingClubMembers applicantMap : applicantMaps) {
//            Profile applicantProfile = applicantMap.getClubApplicant();
//            ProfileDTO applicantDto = new ProfileDTO(
//                    applicantProfile.getUserEntity().getKakaoId(),
//                    applicantProfile.getNickName(),
//                    applicantProfile.getProfileImgUrl()
//            );
//            applicantProfiles.add(applicantDto);
//        }

//        return applicantProfiles;
//    }


    public boolean isAdmin(Long profileId, Long clubId) {
        System.out.println(profileId);
        System.out.println(clubId);

        return readingClubMembersRepository.findByClubMember_IdAndReadingClub_Id(profileId, clubId)
                                           .map(ReadingClubMembers::isAdmin)
                                           .orElse(false);

    }
}
