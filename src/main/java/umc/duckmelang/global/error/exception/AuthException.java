package umc.duckmelang.global.error.exception;


import umc.duckmelang.global.apipayload.code.BaseErrorCode;
import umc.duckmelang.global.error.GeneralException;

public class AuthException extends GeneralException {
    public AuthException(BaseErrorCode errorCode){ super(errorCode); }
}
