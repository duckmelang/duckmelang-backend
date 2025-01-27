package umc.duckmelang.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.eventcategory.domain.EventCategory;
import umc.duckmelang.domain.eventcategory.repository.EventCategoryRepository;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.idolcategory.repository.IdolCategoryRepository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.post.converter.PostConverter;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.dto.PostRequestDto;
import umc.duckmelang.domain.post.repository.PostRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.EventCategoryException;
import umc.duckmelang.global.apipayload.exception.IdolCategoryException;
import umc.duckmelang.global.apipayload.exception.MemberException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final IdolCategoryRepository idolCategoryRepository;

    @Override
    public Post joinPost(PostRequestDto.PostJoinDto request , Long memberId){
//        Member 엔티티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

//        EventCategory 엔티티 조회
        EventCategory eventCategory = eventCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EventCategoryException(ErrorStatus.EVENT_CATEGORY_NOT_FOUND));

//        IdolCategory 엔티티 리스트 조회
        List<IdolCategory> idolCategories = idolCategoryRepository.findAllById(request.getIdolIds());
        if (idolCategories.isEmpty()) {
            throw new IdolCategoryException(ErrorStatus.IDOL_CATEGORY_NOT_FOUND);
        }

        Post newPost =PostConverter.toPost(request, member, eventCategory, idolCategories );
        return postRepository.save(newPost);

    }
}
