package umc.duckmelang.global.apipayload.exception.handler;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;
import umc.duckmelang.global.apipayload.exception.GeneralException;

public class ApplicationHandler extends GeneralException {
    public ApplicationHandler(BaseErrorCode errorCode) {super(errorCode);}
}
