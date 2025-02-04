package umc.duckmelang.domain.member.converter;

import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;

public class MemberProfileConverter {

    public static MemberResponseDto.GetMypageMemberPreviewResultDto toGetMemberPreviewResponseDto(Member member, MemberProfileImage memberProfileImage) {
        return MemberResponseDto.GetMypageMemberPreviewResultDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .age(member.calculateAge())
                .latestPublicMemberProfileImage(memberProfileImage.getMemberImage())
                .build();
    }

    public static MemberResponseDto.GetMypageMemberProfileResultDto toGetMemberProfileResponseDto(Member member, MemberProfileImage memberProfileImage, long postCount, long succeedApplicationCount) {
        return  MemberResponseDto.GetMypageMemberProfileResultDto.builder()
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

    public static Member toUpdateMember(Member member, String updatedNickname, String updatedIntroduction) {
        member.updateProfile(updatedNickname, updatedIntroduction);
        return member;
    }

    public static MemberResponseDto.GetMypageMemberProfileEditResultDto toUpdateMemberProfileDto(Member updatedMember, MemberProfileImage latestPublicMemberProfileImage) {
        return MemberResponseDto.GetMypageMemberProfileEditResultDto.builder()
                .memberId(updatedMember.getId())
                .nickname(updatedMember.getNickname())
                .introduction(updatedMember.getIntroduction())
                .latestPublicMemberProfileImage(latestPublicMemberProfileImage.getMemberImage())
                .build();
    }

    public static MemberResponseDto.OtherProfileDto ToOtherProfileDto(Member member,
                                                                      int postCnt,
                                                                      int matchCnt,
                                                                      MemberProfileImage image){
        return MemberResponseDto.OtherProfileDto.builder()
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
