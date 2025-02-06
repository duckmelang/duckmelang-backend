package umc.duckmelang.domain.member.converter;

import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MyPageResponseDto;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;

public class MemberProfileConverter {

    public static MyPageResponseDto.MypageMemberPreviewResultDto toGetMemberPreviewResponseDto(Member member, MemberProfileImage memberProfileImage) {
        return MyPageResponseDto.MypageMemberPreviewResultDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .age(member.calculateAge())
                .latestPublicMemberProfileImage(memberProfileImage.getMemberImage())
                .build();
    }

    public static MyPageResponseDto.MypageMemberProfileResultDto toGetMemberProfileResponseDto(Member member, MemberProfileImage memberProfileImage, long postCount, long succeedApplicationCount) {
        return  MyPageResponseDto.MypageMemberProfileResultDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .age(member.calculateAge())
                .latestPublicMemberProfileImage(memberProfileImage.getMemberImage())
                .introduction(member.getIntroduction())
                .postCount(postCount)
                .succeedApplicationCount(succeedApplicationCount)
                .build();

    }

    public static MyPageResponseDto.MypageMemberProfileEditResultDto toUpdateMemberProfileDto(Member updatedMember) {
        return MyPageResponseDto.MypageMemberProfileEditResultDto.builder()
                .memberId(updatedMember.getId())
                .nickname(updatedMember.getNickname())
                .introduction(updatedMember.getIntroduction())
                .build();
    }

    public static MyPageResponseDto.MyPageMemberProfileEditBeforeDto toMemberProfileEditBeforeDto(Member member, MemberProfileImage profileImage) {
        return MyPageResponseDto.MyPageMemberProfileEditBeforeDto.builder()
                .nickname(member.getNickname())
                .latestPublicMemberProfileImage(profileImage != null ? profileImage.getMemberImage() : null)
                .build();
    }

    public static MyPageResponseDto.OtherProfileDto ToOtherProfileDto(Member member,
                                                                      int postCnt,
                                                                      int matchCnt,
                                                                      MemberProfileImage image){
        return MyPageResponseDto.OtherProfileDto.builder()
                .nickname(member.getNickname())
                .gender(member.getGender())
                .age(member.calculateAge())
                .introduction(member.getIntroduction())
                .profileImageUrl(image.getMemberImage())
                .postCnt(postCnt)
                .matchCnt(matchCnt)
                .build();
    }
}
