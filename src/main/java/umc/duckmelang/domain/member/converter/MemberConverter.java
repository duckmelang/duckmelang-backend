package umc.duckmelang.domain.member.converter;

import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.dto.MemberResponseDto;

public class MemberConverter {

    public static Member toMember(MemberRequestDto.JoinDto request){
        return Member.builder()
                .loginId(request.getLoginId())
                .password(request.getPassword())
                .build();
    }

    public static MemberResponseDto.JoinResultDto toJoinResultDto(Member member){
        return MemberResponseDto.JoinResultDto.builder()
                .memberId(member.getId())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
