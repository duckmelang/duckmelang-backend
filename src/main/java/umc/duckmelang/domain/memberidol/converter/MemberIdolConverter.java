package umc.duckmelang.domain.memberidol.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.memberidol.domain.MemberIdol;
import umc.duckmelang.domain.memberidol.dto.MemberIdolResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MemberIdolConverter {

    public static MemberIdolResponseDto.IdolDto toIdolDto(MemberIdol memberIdol){
        return MemberIdolResponseDto.IdolDto.builder()
                .idolId(memberIdol.getIdolCategory().getId())
                .idolName(memberIdol.getIdolCategory().getName())
                .idolImage(memberIdol.getIdolCategory().getProfileImage())
                .build();
    }

    public static MemberIdolResponseDto.IdolListDto toIdolListDto(List<MemberIdol> idolList){
        List<MemberIdolResponseDto.IdolDto> idolDtoList = idolList.stream()
                .map(MemberIdolConverter::toIdolDto).collect(Collectors.toList());

        return MemberIdolResponseDto.IdolListDto.builder()
                .idolList(idolDtoList)
                .build();
    }

    public static MemberIdol toMemberIdol(Long memberId, IdolCategory idolCategory){
        return MemberIdol.builder()
                .member(Member.builder().id(memberId).build())
                .idolCategory(idolCategory)
                .build();
    }
}
