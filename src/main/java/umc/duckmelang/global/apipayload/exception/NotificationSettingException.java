package umc.duckmelang.global.apipayload.exception;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;

public class NotificationSettingException extends GeneralException {
    public NotificationSettingException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
