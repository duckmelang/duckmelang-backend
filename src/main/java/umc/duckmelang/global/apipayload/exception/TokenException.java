package umc.duckmelang.global.apipayload.exception;

import lombok.Getter;
import umc.duckmelang.global.apipayload.code.BaseErrorCode;

@Getter
public class TokenException extends RuntimeException {
    private final BaseErrorCode errorCode;

    public TokenException(BaseErrorCode errorCode) {
        super(errorCode.getReasonHttpStatus().getMessage());
        this.errorCode = errorCode;
    }
}

