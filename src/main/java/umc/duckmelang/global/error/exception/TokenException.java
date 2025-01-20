package umc.duckmelang.global.error.exception;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.error.GeneralException;

public class TokenException extends GeneralException {
    public TokenException(BaseErrorCode errorCode){ super(errorCode); }
}