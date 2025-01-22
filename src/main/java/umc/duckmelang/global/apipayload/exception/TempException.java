package umc.duckmelang.global.apipayload.exception;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;

public class TempException extends GeneralException {
    public TempException(BaseErrorCode errorCode) {super(errorCode);}
}