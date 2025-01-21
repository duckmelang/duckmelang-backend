package umc.duckmelang.global.apipayload.exception;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;
import umc.duckmelang.global.apipayload.GeneralException;

public class TokenException extends GeneralException {
    public TokenException(BaseErrorCode errorCode){ super(errorCode); }
}