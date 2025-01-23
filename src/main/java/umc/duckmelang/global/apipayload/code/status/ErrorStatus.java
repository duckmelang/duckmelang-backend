package umc.duckmelang.global.apipayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.duckmelang.global.apipayload.code.BaseErrorCode;
import umc.duckmelang.global.apipayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 멤버 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "존재하지 않는 회원입니다."),
    MEMBER_EMPTY_INTRODUCTION(HttpStatus.BAD_REQUEST, "MEMBER4002", "자기소개는 공란으로 비워둘 수 없습니다."),

    // 멤버 프로필 사진 관련 에러
    MEMBERPROFILEIMAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"MEMBERPROFILEIMAGE4001", "프로필 이미지가 존재하지 않습니다."),

    // 아이돌 카테고리 관련 에러
    IDOL_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "IDOL_CATEGORY4001", "아이돌 카테고리가 없습니다."),
    INVALID_IDOLCATEGORY(HttpStatus.BAD_REQUEST, "IDOLCATEGORY4002", "선택한 아이돌 중 유효하지 않은 항목이 있습니다."),

    // 행사 카테고리 관련 에러
    INVALID_EVENTCATEGORY(HttpStatus.BAD_REQUEST, "EVENTCATEGORY4001", "선택한 행사 중 유효하지 않은 항목이 있습니다."),

    // 지뢰 관련 에러
    DUPLICATE_LANDMINE(HttpStatus.BAD_REQUEST, "LANDMINE4001", "중복된 키워드가 존재합니다."),

    //동행요청 관련 에러 
    ALREADY_PROCESSED_APPLICATION(HttpStatus.CONFLICT, "APPLICATION4001","이미 처리된 동행 요청입니다."),
    NOT_POSSESSED_APPLICATION(HttpStatus.CONFLICT, "APPLICATION4002", "본인 소유의 동행 요청이 아닙니다."),
    APPLICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "APPLICATION4003", "존재하지 않는 동행 요청입니다."),

    // 테스트 응답
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트");

   
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }


}
