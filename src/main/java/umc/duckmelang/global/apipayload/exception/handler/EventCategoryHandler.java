package umc.duckmelang.global.apipayload.exception.handler;

import umc.duckmelang.global.apipayload.code.BaseErrorCode;
import umc.duckmelang.global.apipayload.exception.GeneralException;

public class EventCategoryHandler extends GeneralException {

    public EventCategoryHandler(BaseErrorCode errorCode) {super(errorCode);}
}
