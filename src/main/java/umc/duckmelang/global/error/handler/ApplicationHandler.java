package umc.duckmelang.global.error.handler;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;
import umc.duckmelang.global.error.GeneralException;

public class ApplicationHandler extends GeneralException {
    public ApplicationHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
