package umc.duckmelang.domain.member.converter;

import umc.duckmelang.domain.eventcategory.domain.EventCategory;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.landmine.domain.Landmine;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.member.service.MemberQueryService;
import umc.duckmelang.domain.memberevent.domain.MemberEvent;
import umc.duckmelang.domain.memberidol.domain.MemberIdol;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageQueryService;

import java.util.List;
import java.util.stream.Collectors;

public class MemberConverter {


    public static MemberIdol toMemberIdol(Member member, IdolCategory idolCategory) {
        return MemberIdol.builder()
                .member(member)
                .idolCategory(idolCategory)
                .build();
    }

    public static MemberResponseDto.SelectIdolsResultDto toSelectIdolResponseDto(List<MemberIdol> memberIdolList) {

        Member member = memberIdolList.get(0).getMember(); // 반환된 리스트 내 모든 MemberIdol은 같은 Member를 참조하고 있음을 전제

        List<Long> idolCategoryIds = memberIdolList.stream()
                .map(memberIdol -> memberIdol.getIdolCategory().getId())
                .toList();

        return MemberResponseDto.SelectIdolsResultDto.builder()
                .memberId(member.getId())
                .idolCategoryIds(idolCategoryIds)
                .build();
    }

    public static MemberEvent toMemberEvent(Member member, EventCategory eventCategory) {
        return MemberEvent.builder()
                .member(member)
                .eventCategory(eventCategory)
                .build();
    }

    public static MemberResponseDto.SelectEventsResultDto toSelectEventResponseDto(List<MemberEvent> memberEventList) {

        Member member = memberEventList.get(0).getMember(); // 반환된 리스트 내 모든 MemberEvent는 같은 Member를 참조하고 있음을 전제

        List<Long> eventCategoryIds = memberEventList.stream()
                .map(memberEvent -> memberEvent.getEventCategory().getId())
                .toList();

        return MemberResponseDto.SelectEventsResultDto.builder()
                .memberId(member.getId())
                .eventCategoryIds(eventCategoryIds)
                .build();

    }

    public static Landmine toLandmine(Member member, String content) {
        return Landmine.builder()
                .member(member)
                .content(content)
                .build();
    }

    public static MemberResponseDto.CreateLandmineResultDto toCreateLandmineResponseDto(List<Landmine> landmineList) {

        Member member = landmineList.get(0).getMember(); // 반환된 리스트 내 모든 MemberEvent는 같은 Member를 참조하고 있음을 전제

        List<String> landmineContents = landmineList.stream()
                .map(Landmine::getContent)
                .collect(Collectors.toList());

        return MemberResponseDto.CreateLandmineResultDto.builder()
                .memberId(member.getId())
                .landmineContents(landmineContents)
                .build();

    }

    public static MemberProfileImage toMemberProfileImage(Member member, String profileImageUrl) {
        return MemberProfileImage.builder()
                .member(member)
                .memberImage(profileImageUrl)
                .isPublic(true)  // 새로운 프로필 이미지 등록 시에는 항상 전체공개 속성으로 생성됨
                .build();    }

    public static MemberResponseDto.CreateMemberProfileImageResultDto toCreateMemberProfileImageResponseDto(MemberProfileImage memberProfileImage) {

        return MemberResponseDto.CreateMemberProfileImageResultDto.builder()
                .memberId(memberProfileImage.getMember().getId())
                .memberProfileImageURL(memberProfileImage.getMemberImage())
                .isPublic(memberProfileImage.isPublic())
                .build();
    }

    public static Member toMemberWithIntroduction(Member member, String introduction) {
        return member.withIntroduction(introduction);
    }

    public static MemberResponseDto.CreateIntroductionResultDto toCreateIntroductionResponseDto(Member member) {

        return MemberResponseDto.CreateIntroductionResultDto.builder()
                .memberId(member.getId())
                .introduction(member.getIntroduction())
                .build();

    }

    public static MemberResponseDto.GetMypageMemberPreviewResultDto toGetMemberPreviewResponseDto(Member member, MemberProfileImage memberProfileImage) {

        return MemberResponseDto.GetMypageMemberPreviewResultDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .age(member.calculateAge())
                .latestPublicMemberProfileImage(memberProfileImage.getMemberImage())
                .build();
    }
}
