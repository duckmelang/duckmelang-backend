package umc.duckmelang.global.error.exception;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;
import umc.duckmelang.global.error.GeneralException;

public class MemberException extends GeneralException {
    public MemberException(BaseErrorCode errorCode) {super(errorCode);}
}