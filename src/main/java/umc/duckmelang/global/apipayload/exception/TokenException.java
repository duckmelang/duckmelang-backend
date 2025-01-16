package umc.duckmelang.global.apipayload.exception;

import lombok.Getter;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;

@Getter
public class TokenException extends RuntimeException{
    private final ErrorStatus errorStatus;

    public TokenException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}
