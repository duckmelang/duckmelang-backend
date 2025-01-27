package umc.duckmelang.global.apipayload.exception.handler;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;
import umc.duckmelang.global.apipayload.exception.GeneralException;

public class LandmineHandler extends GeneralException {

    public LandmineHandler(BaseErrorCode errorCode) {super(errorCode);}
}
