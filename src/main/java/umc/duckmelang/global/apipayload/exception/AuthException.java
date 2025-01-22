package umc.duckmelang.global.apipayload.exception;


import umc.duckmelang.global.apipayload.code.BaseErrorCode;

public class AuthException extends GeneralException {
    public AuthException(BaseErrorCode errorCode){ super(errorCode); }
}
