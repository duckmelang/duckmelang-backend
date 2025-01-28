package umc.duckmelang.domain.application.controller;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.duckmelang.domain.application.converter.ApplicationConverter;
import umc.duckmelang.domain.application.domain.Application;
import umc.duckmelang.domain.application.service.ApplicationCommandService;
import umc.duckmelang.domain.application.service.ApplicationQueryService;
import umc.duckmelang.domain.application.validation.annotation.ExistsApplication;
import umc.duckmelang.domain.materelationship.domain.MateRelationship;
import umc.duckmelang.global.annotations.CommonApiResponses;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.domain.application.dto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
@Validated
public class ApplicationRestController {
    private final ApplicationCommandService applicationCommandService;
    private final ApplicationQueryService applicationQueryService;

    @PostMapping("/received/failed/{applicationId}")
    @CommonApiResponses
    @Operation(summary = "받은 동행요청 거절 API",description = "path variable로 상태를 변경하고자 하는 동행요청 id를 받습니다.\nrequest body로 member id(추후 JWT로 교체)를 받습니다.")
    public ApiResponse<ApplicationResponseDto.ApplicationStatusChangeResponseDto> failApplication(@PathVariable @ExistsApplication Long applicationId,
                                                                                                  @RequestParam Long memberId  // 임시로 사용. 나중에 JWT에서 추출할 예정
                                                                                                  ) {
        Application application = applicationCommandService.updateStatusToFailed(applicationId, memberId);
        return ApiResponse.onSuccess(ApplicationConverter.toApplicationStatusChangeResponseDto(application));
    }

    @PostMapping("/sent/canceled/{applicationId}")
    @CommonApiResponses
    @Operation(summary = "보낸 동행요청 취소 API",description = "path variable로 상태를 변경하고자 하는 동행요청 id를 받습니다.\nrequest body로 member id(추후 JWT로 교체)를 받습니다.")
    public ApiResponse<ApplicationResponseDto.ApplicationStatusChangeResponseDto> cancelApplication(@PathVariable @ExistsApplication Long applicationId,
                                                                                                  @RequestParam Long memberId  // 임시로 사용. 나중에 JWT에서 추출할 예정
    ) {
        Application application = applicationCommandService.updateStatusToCanceled(applicationId, memberId);
        return ApiResponse.onSuccess(ApplicationConverter.toApplicationStatusChangeResponseDto(application));
    }

    @PostMapping("/received/succeed/{applicationId}")
    @CommonApiResponses
    @Operation(summary = "받은 동행요청 수락 API",description = "path variable로 상태를 변경하고자 하는 동행요청 id를 받습니다.\nrequest body로 member id(추후 JWT로 교체)를 받습니다.")
    public ApiResponse<ApplicationResponseDto.MateRelationshipCreateResponseDto> succeedApplication(@PathVariable @ExistsApplication Long applicationId,
                                                                                                  @RequestParam Long memberId  // 임시로 사용. 나중에 JWT에서 추출할 예정
    ) {
        MateRelationship mateRelationship = applicationCommandService.updateStatusToSucceed(applicationId, memberId);
        return ApiResponse.onSuccess(ApplicationConverter.toMateRelationshipCreateResponseDto(mateRelationship));
    }

    @GetMapping("/received")
    @CommonApiResponses
    @Operation(summary = "받은 수락/거절 동행요청 조회 API",description = "request body로 'post를 올린 member id'(추후 JWT로 교체)를 받고, 수락/거절 상태인 동행요청을 조회합니다.")
    public ApiResponse<ApplicationResponseDto.ReceivedApplicationListDto> getReceivedApplicationList(@RequestParam Long memberId, @RequestParam(name = "page") Integer page){
        Page<ReceivedApplicationDto> dto =  applicationQueryService.getReceivedApplicationListExceptPending(memberId, page);
        return ApiResponse.onSuccess(ApplicationConverter.toReceivedApplicationListDto(dto));
    }

    @GetMapping("/received/pending")
    @CommonApiResponses
    @Operation(summary = "대기중인 받은 동행요청 조회 API",description = "request body로 'post를 올린 member id'(추후 JWT로 교체)를 받습니다.")
    public ApiResponse<ApplicationResponseDto.ReceivedApplicationListDto> getReceivedPendingApplicationList(@RequestParam Long memberId, @RequestParam(name = "page") Integer page){
        Page<ReceivedApplicationDto> dto =  applicationQueryService.getReceivedPendingApplicationList(memberId, page);
        return ApiResponse.onSuccess(ApplicationConverter.toReceivedApplicationListDto(dto));
    }

    @GetMapping("/sent")
    @Operation(summary = "보낸 동행요청 조회 API",description = "request body로 '동행요청을 보낸 member id'(추후 JWT로 교체)를 받습니다.")
    @CommonApiResponses
    public ApiResponse<ApplicationResponseDto.SentApplicationListDto> getSentApplicationList(@RequestParam Long memberId, @RequestParam(name = "page") Integer page){
        Page<SentApplicationDto> dto = applicationQueryService.getSentApplicationList(memberId, page);
        return ApiResponse.onSuccess(ApplicationConverter.toSentApplicationListDto(dto));
    }
}
