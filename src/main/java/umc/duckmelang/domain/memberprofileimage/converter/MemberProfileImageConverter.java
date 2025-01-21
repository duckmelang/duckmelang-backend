package umc.duckmelang.domain.memberprofileimage.converter;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.dto.MemberProfileImageResponseDto;

import java.util.stream.Collectors;
import java.util.List;

public class MemberProfileImageConverter {
    public static MemberProfileImageResponseDto.MemberProfileImageListDto toMemberProfileImageListDto(Page<MemberProfileImage> page){
        List<MemberProfileImageResponseDto.MemberProfileImageDto> list = page.stream()
                .map(MemberProfileImageConverter::toMemberProfileImageDto)
                .collect(Collectors.toList());
        return MemberProfileImageResponseDto.MemberProfileImageListDto.builder()
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
}
