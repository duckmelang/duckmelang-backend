package umc.duckmelang.global.apipayload.exception;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;
import umc.duckmelang.global.apipayload.GeneralException;

public class MemberException extends GeneralException {
    public MemberException(BaseErrorCode errorCode) {super(errorCode);}
}