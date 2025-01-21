package umc.duckmelang.domain.member.converter;

import org.springframework.data.domain.Page;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberResponseDto;
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.postimage.converter.PostImageConverter;
import umc.duckmelang.domain.postimage.dto.PostImageResponseDto;
import umc.duckmelang.domain.postimage.dto.PostThumbnailResponseDto;

import java.util.List;
public class MemberConverter {
    public static MemberResponseDto.OtherProfileDto ToOtherProfileDto(Member member,
                                                                      int postCnt,
                                                                      int matchCnt,
                                                                      MemberProfileImage image,
                                                                      Page<PostThumbnailResponseDto> imagePage){
        return MemberResponseDto.OtherProfileDto.builder()
                .nickname(member.getNickname())
                .gender(member.getGender()?"Female":"Male")
                .age(member.getAge())
                .introduction(member.getIntroduction())
                .profileImageUrl(image.getMemberImage())
                .postCnt(postCnt)
                .matchCnt(matchCnt)
                .postsThumbnail(PostImageConverter.toPostThumbnailListResponseDto(imagePage))
                .build();
    }
}
