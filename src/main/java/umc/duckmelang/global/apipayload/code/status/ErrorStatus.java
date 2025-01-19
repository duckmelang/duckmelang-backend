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

    // 테스트 응답
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),

    // 토큰 관련 응답
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN4000", "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN4001", "유효하지 않은 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "TOKEN4004", "토큰을 찾을 수 없습니다."),

    // 로그인 관련 응답
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH4000", "인증 정보가 필요합니다."),
    AUTH_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH4001", "이메일 또는 비밀번호가 잘못되었습니다."),
    AUTH_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH4004", "이메일과 일치하는 사용자가 없습니다."),

    // 회원 관련 응답
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER4001", "이미 존재하는 이메일입니다."),

    // idolCategory Error
    IDOL_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "IDOL_CATEGORY4001", "아이돌 카테고리가 없습니다."),

    // Member
    NO_SUCH_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER4001", "존재하지 않는 멤버입니다."),

    // 동행요청
    ALREADY_PROCESSED_APPLICATION(HttpStatus.CONFLICT, "APPLICATION4001","이미 처리된 동행 요청입니다."),
    NOT_POSSESSED_APPLICATION(HttpStatus.CONFLICT, "APPLICATION4002", "본인 소유의 동행 요청이 아닙니다."),
    APPLICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "APPLICATION4003", "존재하지 않는 동행 요청입니다.")
    ;
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
