package umc.duckmelang.domain.memberprofileimage.converter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.landmine.domain.Landmine;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageRequestDto;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageResponseDto;

import java.util.stream.Collectors;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberProfileImageConverter {

    public static MemberProfileImageResponseDto.GetAllProfileImageResultDto toGetAllProfileImageResultDto(Page<MemberProfileImage> page) {

        List<MemberProfileImageResponseDto.MemberProfileImageDto> list = page.stream()
                .map(MemberProfileImageConverter::toMemberProfileImageDto)
                .collect(Collectors.toList());

        return MemberProfileImageResponseDto.GetAllProfileImageResultDto.builder()
                .profileImageList(list)
                .totalPage(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .listSize(list.size())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .build();
    }


    public static MemberProfileImageResponseDto.MemberProfileImageDto toMemberProfileImageDto(MemberProfileImage memberProfileImage){
        return MemberProfileImageResponseDto.MemberProfileImageDto.builder()
                .memberProfileImageUrl(memberProfileImage.getMemberImage())
                .createdAt(memberProfileImage.getCreatedAt())
                .build();
    }

    public static MemberProfileImageResponseDto.DeleteProfileImageResultDto toDeleteProfileImageResultDto() {
        return MemberProfileImageResponseDto.DeleteProfileImageResultDto.builder()
                .succeedMessage("프로필 사진이 성공적으로 삭제되었습니다.")
                .build();
    }

    public static MemberProfileImage toMemberProfileImageWithChangedStatus(Long imageId, boolean isPublic) {
        return MemberProfileImage.builder()
                .id(imageId)
                .isPublic(isPublic)
                .build();
    }

    public static MemberProfileImageResponseDto.UpdateProfileImageStatusResultDto toUpdateProfileImageStatusResultDto(MemberProfileImage updatedMemberProfileImage) {
        String changedStatus;

        if (updatedMemberProfileImage.isPublic()) {
            changedStatus = "PUBLIC";
        } else {
            changedStatus ="PRIVATE";
        }

        return MemberProfileImageResponseDto.UpdateProfileImageStatusResultDto.builder()
                .changedStatus(changedStatus)
                .build();
    }

    public static MemberProfileImage toCreateMemberProfileImage(Member member, String memberProfileImageURL) {
        return MemberProfileImage.builder()
                .memberImage(memberProfileImageURL)
                .member(member)
                .isPublic(true)
                .build();
    }
}
