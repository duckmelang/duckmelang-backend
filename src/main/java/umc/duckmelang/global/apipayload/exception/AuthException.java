package umc.duckmelang.global.apipayload.exception;


import umc.duckmelang.global.apipayload.code.BaseErrorCode;
import umc.duckmelang.global.apipayload.GeneralException;

public class AuthException extends GeneralException {
    public AuthException(BaseErrorCode errorCode){ super(errorCode); }
}
