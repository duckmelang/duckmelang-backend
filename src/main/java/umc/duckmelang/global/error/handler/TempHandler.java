package umc.duckmelang.global.error.handler;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;
import umc.duckmelang.global.error.GeneralException;

public class TempHandler extends GeneralException {
    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
