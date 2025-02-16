package umc.duckmelang.global.apipayload.exception;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;

public class NotificationException extends GeneralException {
    public NotificationException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}