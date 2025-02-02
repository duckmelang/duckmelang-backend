package umc.duckmelang.global.apipayload.exception;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;

public class PostImageException extends GeneralException {
    public PostImageException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
