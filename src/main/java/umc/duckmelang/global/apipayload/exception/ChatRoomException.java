package umc.duckmelang.global.apipayload.exception;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;

public class ChatRoomException extends GeneralException {
    public ChatRoomException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
