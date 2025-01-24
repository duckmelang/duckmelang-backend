package umc.duckmelang.domain.memberprofileimage.converter;

import org.springframework.stereotype.Component;
import umc.duckmelang.domain.landmine.domain.Landmine;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberProfileImageConverter {

    public static MemberProfileImageResponseDto.GetAllProfileImageResultDto toGetAllProfileImageResultDto(List<MemberProfileImage> memberProfileImageList) {
        Member member = memberProfileImageList.get(0).getMember(); // 반환된 리스트 내 모든 프로필 사진은 같은 Member를 참조하고 있음을 전제

        List<String> profileImageUrls = memberProfileImageList.stream()
                .map(MemberProfileImage::getMemberImage)
                .collect(Collectors.toList());

        return MemberProfileImageResponseDto.GetAllProfileImageResultDto.builder()
                .memberId(member.getId())
                .profileImageUrls(profileImageUrls)
                .build();
    }

    public static MemberProfileImageResponseDto.DeleteProfileImageResultDto toDeleteProfileImageResultDto(Long memberId, Long userProfileImageId) {
        return MemberProfileImageResponseDto.DeleteProfileImageResultDto.builder()
                .memberId(memberId)
                .deletedMemberProfileImageId(userProfileImageId)
                .succeedMessage("프로필 사진이 성공적으로 삭제되었습니다.")
                .build();
    }
}
