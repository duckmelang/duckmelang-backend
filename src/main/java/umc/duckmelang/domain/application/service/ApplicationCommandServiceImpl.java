package umc.duckmelang.domain.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.application.domain.enums.ApplicationStatus;
import umc.duckmelang.domain.application.repository.ApplicationRepository;
import umc.duckmelang.domain.chatroom.domain.ChatRoom;
import umc.duckmelang.domain.chatroom.domain.enums.ChatRoomStatus;
import umc.duckmelang.domain.chatroom.repository.ChatRoomRepository;
import umc.duckmelang.domain.materelationship.domain.MateRelationship;
import umc.duckmelang.domain.materelationship.repository.MateRelationshipRepository;
import umc.duckmelang.domain.member.domain.Member;
import umc.duckmelang.domain.member.repository.MemberRepository;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.repository.PostRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.ApplicationException;
import umc.duckmelang.global.apipayload.exception.MemberException;
import umc.duckmelang.global.apipayload.exception.PostException;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ApplicationCommandServiceImpl implements ApplicationCommandService{
    private final ApplicationRepository applicationRepository;
    private final MateRelationshipRepository mateRelationshipRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Application makeNewApplication(Long postId, Long memberId) {
        commonProcess(postId, memberId);

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(ErrorStatus.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        Application application = Application.builder()
                .post(post)
                .member(member)
                .status(ApplicationStatus.PENDING)
                .build();
        return application;
    }

    @Override
    @Transactional
    public Application updateStatusToFailed(Long applicationId, Long memberId) {
        // TO-DO : authorize JWT

        Application application = applicationRepository
                .findByIdAndPostMemberId(applicationId, memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.APPLICATION_NOT_FOUND));

        if (!application.updateStatus(ApplicationStatus.FAILED)) {
            throw new ApplicationException(ErrorStatus.ALREADY_PROCESSED_APPLICATION);
        }

        applicationRepository.save(application);
        return application;
    }

    @Override
    @Transactional
    public Application updateStatusToCanceled(Long applicationId, Long memberId) {
        // TO-DO : authorize JWT

        Application application = applicationRepository
                .findByIdAndMemberId(applicationId, memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.APPLICATION_NOT_FOUND));

        if (!application.updateStatus(ApplicationStatus.CANCELED)) {
            throw new ApplicationException(ErrorStatus.ALREADY_PROCESSED_APPLICATION);
        }

        applicationRepository.save(application);
        return application;
    }

    @Override
    @Transactional
    public MateRelationship updateStatusToSucceed(Long applicationId, Long memberId) {

        Application application = applicationRepository
                .findByIdAndPostMemberId(applicationId, memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.APPLICATION_NOT_FOUND));

        //공통
        commonProcess(application.getPost().getId(), memberId);

        //application.status = SUCCEED
        if (!application.updateStatus(ApplicationStatus.SUCCEED)) {
            throw new ApplicationException(ErrorStatus.ALREADY_PROCESSED_APPLICATION);
        }

        //MateRelationship 생성
        MateRelationship newRelationship = MateRelationship.builder()
                .firstMember(application.getPost().getMember())
                .secondMember(application.getMember())
                .application(application).build();

        application.setMateRelationship(newRelationship);

        //post
        Post post = application.getPost();
        post.toggleWanted();

        //chatRoom

        mateRelationshipRepository.save(newRelationship);
        applicationRepository.save(application);

        return newRelationship;
    }

    private void commonProcess(Long postId, Long memberId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(ErrorStatus.POST_NOT_FOUND));

        if (post.getWanted() == 0 || applicationRepository.existsByPostIdAndStatus(postId, ApplicationStatus.PENDING)) {
            Optional<ChatRoom> chatRoom = chatRoomRepository.findByPostIdAndOtherMemberId(postId,memberId);
            chatRoom.ifPresent(c -> c.setChatRoomStatus(ChatRoomStatus.TERMINATED));

            throw new ApplicationException(ErrorStatus.UNAVAILABLE_TO_PROCESS_APPLICATION);
        }

    }

}
