package umc.duckmelang.domain.member.service;

import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.post.domain.Post;

import java.util.Optional;

public interface MemberQueryService {

    boolean existsById(Long memberId);
    Optional<Member> findById(Long memberId);
}
