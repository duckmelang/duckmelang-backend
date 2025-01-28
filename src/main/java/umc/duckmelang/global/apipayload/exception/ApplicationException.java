package umc.duckmelang.global.apipayload.exception;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;

public class ApplicationException extends GeneralException {
    public ApplicationException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
