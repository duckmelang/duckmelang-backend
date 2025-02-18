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
import umc.duckmelang.domain.memberprofileimage.domain.MemberProfileImage;
import umc.duckmelang.domain.memberprofileimage.service.MemberProfileImageQueryService;
import umc.duckmelang.domain.notification.service.NotificationCommandService;
import umc.duckmelang.domain.notificationsetting.domain.NotificationSetting;
import umc.duckmelang.domain.notificationsetting.service.NotificationSettingQueryService;
import umc.duckmelang.domain.post.domain.Post;
import umc.duckmelang.domain.post.repository.PostRepository;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.ApplicationException;
import umc.duckmelang.global.apipayload.exception.MemberException;
import umc.duckmelang.global.apipayload.exception.MemberProfileImageException;
import umc.duckmelang.global.apipayload.exception.PostException;

import java.util.Optional;

import static umc.duckmelang.domain.notification.domain.enums.NotificationType.REQUEST;
import static umc.duckmelang.domain.notification.domain.enums.NotificationType.REVIEW;

@Service
@RequiredArgsConstructor
public class ApplicationCommandServiceImpl implements ApplicationCommandService{
    private final ApplicationRepository applicationRepository;
    private final MateRelationshipRepository mateRelationshipRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final NotificationSettingQueryService notificationSettingQueryService;
    private final NotificationCommandService notificationCommandService;
    private final MemberProfileImageQueryService memberProfileImageQueryService;


    @Override
    @Transactional
    public Application makeNewApplication(Long postId, Long memberId) {
        validateApplyingCondition(postId, memberId);

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(ErrorStatus.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        //이미 동행요청 보낸 적 있는지
        if (applicationRepository.existsByPostIdAndMemberId(postId, memberId))
            throw new ApplicationException(ErrorStatus.ALREADY_PROCESSED_APPLICATION);

        //자신의 post는 아닌지
        if (post.getMember() == member)
            throw new ApplicationException(ErrorStatus.UNAVAILABLE_TO_APPLY_FOR_OWN_POST);

        //알림 보낼때 member profile image
        Optional<MemberProfileImage> profileImageOptional = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId);
        String profileImageUrl = profileImageOptional
                .map(MemberProfileImage::getMemberImage)  // memberImage 필드를 가져옴
                .orElseThrow(() -> new MemberProfileImageException(ErrorStatus.MEMBER_PROFILE_IMAGE_NOT_FOUND));

        //        request 알림 켜져있을때만 전송
        NotificationSetting notificationSetting = notificationSettingQueryService.findNotificationSetting(post.getMember().getId());
        if (notificationSetting.getRequestNotificationEnabled()) {
            notificationCommandService.send(member, post.getMember(), REQUEST, member.getNickname() + " 님이 동행을 요청했어요", profileImageUrl);
        }

        Application application = Application.builder()
                .post(post)
                .member(member)
                .status(ApplicationStatus.PENDING)
                .build();
        return applicationRepository.save(application);
    }

    @Override
    @Transactional
    public Application updateStatusToFailed(Long applicationId, Long memberId) {
        Application application = applicationRepository
                .findByIdAndPostMemberId(applicationId, memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.APPLICATION_NOT_FOUND));


        if (!application.updateStatus(ApplicationStatus.FAILED)) {
            throw new ApplicationException(ErrorStatus.ALREADY_PROCESSED_APPLICATION);
        }

        //알림 보낼때 member profile image
        Optional<MemberProfileImage> profileImageOptional = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId);
        String profileImageUrl = profileImageOptional
                .map(MemberProfileImage::getMemberImage)  // memberImage 필드를 가져옴
                .orElseThrow(() -> new MemberProfileImageException(ErrorStatus.MEMBER_PROFILE_IMAGE_NOT_FOUND));

        Member sender = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        Member receiver = memberRepository.findById(application.getMember().getId()).orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        //        request 알림 켜져있을때만 전송
        NotificationSetting notificationSetting = notificationSettingQueryService.findNotificationSetting(receiver.getId());
        if (notificationSetting.getRequestNotificationEnabled()) {
            notificationCommandService.send(receiver, sender, REQUEST, sender.getNickname() + " 님이 동행 요청을 거절했어요", profileImageUrl);
        }

        //ChatRoom.status = TERMINATED
        chatRoomRepository.updateStatusByPostId(application.getPost().getId(), memberId, ChatRoomStatus.TERMINATED);

        applicationRepository.save(application);
        return application;
    }

    @Override
    @Transactional
    public Application updateStatusToCanceled(Long applicationId, Long memberId) {
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
        validateApplyingCondition(application.getPost().getId(), memberId);

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

        //알림 보낼때 member profile image
        Optional<MemberProfileImage> profileImageOptional = memberProfileImageQueryService.getLatestPublicMemberProfileImage(memberId);
        String profileImageUrl = profileImageOptional
                .map(MemberProfileImage::getMemberImage)  // memberImage 필드를 가져옴
                .orElseThrow(() -> new MemberProfileImageException(ErrorStatus.MEMBER_PROFILE_IMAGE_NOT_FOUND));

        Member sender = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));
        Member receiver = memberRepository.findById(application.getMember().getId()).orElseThrow(() -> new MemberException(ErrorStatus.MEMBER_NOT_FOUND));

        //        request 알림 켜져있을때만 전송
        NotificationSetting notificationSetting = notificationSettingQueryService.findNotificationSetting(receiver.getId());
        if (notificationSetting.getRequestNotificationEnabled()) {
            notificationCommandService.send(receiver, sender, REQUEST, sender.getNickname() + " 님이 동행 요청을 수락했어요", profileImageUrl);
        }

        //chatRoom
        // 선택된 채팅방을 CONFIRMED로 변경
        chatRoomRepository.updateStatusByPostId(post.getId(), memberId, ChatRoomStatus.CONFIRMED);

        // 나머지 채팅방들을 TERMINATED로 변경
        chatRoomRepository.updateStatusByNonPostId(post.getId(), memberId, ChatRoomStatus.TERMINATED);

        mateRelationshipRepository.save(newRelationship);
        applicationRepository.save(application);
        postRepository.save(post);

        return newRelationship;
    }

    private void validateApplyingCondition(Long postId, Long memberId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(ErrorStatus.POST_NOT_FOUND));

        if (post.getWanted() == 0 || applicationRepository.existsByPostIdAndStatus(postId, ApplicationStatus.SUCCEED)) {
            Optional<ChatRoom> chatRoom = chatRoomRepository.findByPostIdAndOtherMemberId(postId,memberId);
            chatRoom.ifPresent(c -> c.setChatRoomStatus(ChatRoomStatus.TERMINATED));

            throw new ApplicationException(ErrorStatus.UNAVAILABLE_TO_APPLY_FOR_CONFIRMED_POST);
        }

    }

}
