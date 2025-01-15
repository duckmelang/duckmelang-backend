package umc.duckmelang.domain.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.domain.eventcategory.domain.EventCategory;
import umc.duckmelang.domain.eventcategory.repository.EventCategoryRepository;
import umc.duckmelang.domain.idolcategory.domain.IdolCategory;
import umc.duckmelang.domain.idolcategory.repository.IdolCategoryRepository;
import umc.duckmelang.domain.landmine.domain.Landmine;
import umc.duckmelang.domain.landmine.repository.LandmineRepository;
import umc.duckmelang.domain.member.converter.MemberConverter;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.dto.MemberRequestDto;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.memberevent.domain.MemberEvent;
import umc.duckmelang.domain.memberevent.repository.MemberEventRepository;
import umc.duckmelang.domain.memberidol.domain.MemberIdol;
import umc.duckmelang.domain.memberidol.repository.MemberIdolRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final IdolCategoryRepository idolCategoryRepository;
    private final MemberIdolRepository memberIdolRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final MemberEventRepository memberEventRepository;
    private final LandmineRepository landmineRepository;


    @Override
    @Transactional
    public List<MemberIdol> selectIdols(Long memberId, MemberRequestDto.SelectIdolsDto request) {
        // 회원 조회 및 유효성 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 아이돌 카테고리 조회 및 유효성 검증
        List<IdolCategory> idolCategoryList = idolCategoryRepository.findAllById(request.getIdolCategoryIds());
        if (idolCategoryList.size() != request.getIdolCategoryIds().size()) {
            throw new IllegalArgumentException("선택한 아이돌 중 유효하지 않은 항목이 있습니다.");
        }

        // 기존 데이터 존재 시 삭제
        memberIdolRepository.deleteAllByMember(member);
        // 새 데이터 저장
        List<MemberIdol> memberIdolList = idolCategoryList.stream()
                .map(idolCategory -> MemberConverter.toMemberIdol(member, idolCategory))
                .toList();

        return memberIdolRepository.saveAll(memberIdolList);
    }

    @Override
    @Transactional
    public List<MemberEvent> selectEvents(Long memberId, MemberRequestDto.SelectEventsDto request) {
        // 회원 조회 및 유효성 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 선호하는 행사를 하나도 고르지 않은 경우, 아래 로직을 진행하지 않고 바로 빈 리스트를 return
        if (request.getEventCategoryIds() == null || request.getEventCategoryIds().isEmpty()) {
            return Collections.emptyList();
        }

        //행사 카테고리 조회 및 유효성 검증
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAllById(request.getEventCategoryIds());
        if (eventCategoryList.size() != request.getEventCategoryIds().size()) {
            throw new IllegalArgumentException("선택한 이벤트 중 유효하지 않은 항목이 있습니다.");
        }

        // 기존 데이터 존재 시 삭제
        memberEventRepository.deleteAllByMember(member);

        // 새 데이터 저장
        List<MemberEvent> memberEventList = eventCategoryList.stream()
                .map(eventCategory -> MemberConverter.toMemberEvent(member, eventCategory))
                .toList();

        return memberEventRepository.saveAll(memberEventList);
    }

    @Override
    @Transactional
    public List<Landmine> selectLandmines(Long memberId, MemberRequestDto.SelectLandminesDto request) {
        // 회원 조회 및 유효성 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 지뢰를 하나도 설정하지 않은 경우, 아래 로직을 진행하지 않고 바로 빈 리스트를 return
        if (request.getLandmineContents() == null || request.getLandmineContents().isEmpty()) {
            return Collections.emptyList();
        }

        // 지뢰 내용을 가져온다
        List<String> landmineContents = request.getLandmineContents();
        // 지뢰 내용을 검증하여 중복된 키워드가 있는지 체크하고, 있다면 에러 발생
        Set<String> uniqueContents = new HashSet<>();
        for (String content : landmineContents) {
            if (!uniqueContents.add(content)) {
                throw new IllegalArgumentException("중복된 키워드가 존재합니다: " + content);
            }
        }

        // 기존 데이터 존재 시 삭제
        landmineRepository.deleteAllByMember(member);

        // 새 데이터 저장
        List<Landmine> landmineList = request.getLandmineContents().stream()
                .map(content -> MemberConverter.toLandmine(member, content))
                .collect(Collectors.toList());

        return landmineRepository.saveAll(landmineList);
    }


}
