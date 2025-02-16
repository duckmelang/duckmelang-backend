package umc.duckmelang.global.apipayload.exception;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;

public class ChatMessageException extends GeneralException {
    public ChatMessageException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}