package umc.duckmelang.apipayload.exception.handler;

import umc.duckmelang.apipayload.code.BaseErrorCode;
import umc.duckmelang.apipayload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
