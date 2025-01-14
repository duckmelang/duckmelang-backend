package umc.duckmelang.domain.member.converter;

import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.dto.MemberResponseDto;

public class MemberConverter {

    public static Member toMember(MemberRequestDto.SignupDto request){
        return Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public static MemberResponseDto.SignupResultDto toSignupResultDto(Member member){
        return MemberResponseDto.SignupResultDto.builder()
                .memberId(member.getId())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
