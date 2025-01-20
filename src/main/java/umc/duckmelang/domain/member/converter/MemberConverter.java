package umc.duckmelang.domain.member.converter;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import java.util.List;
public class MemberConverter {
    public static MemberResponseDto.OtherProfileDto ToOtherProfileDto(Member member, int postCnt, int matchCnt, Page<String> imagePage){
        return MemberResponseDto.OtherProfileDto.builder()
                .nickname(member.getNickname())
                .gender(member.getGender()?"Female":"Male")
                .age(member.getAge())
                .introduction(member.getIntroduction())
                .postCnt(postCnt)
                .matchCnt(matchCnt)
                .memberProfileImageList(imagePage.getContent())
                .listSize(imagePage.getSize())
                .totalPage(imagePage.getTotalPages())
                .totalElements(imagePage.getTotalElements())
                .isFirst(imagePage.isFirst())
                .isLast(imagePage.isLast())
                .build();
    }
}
