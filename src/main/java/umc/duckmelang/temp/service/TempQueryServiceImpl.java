package umc.duckmelang.temp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.apipayload.code.status.ErrorStatus;
import umc.duckmelang.apipayload.exception.handler.TempHandler;

@Service
@RequiredArgsConstructor
public class TempQueryServiceImpl implements TempQueryService {

    @Override
    public void CheckFlag(Integer flag){
        if(flag == 1)
            throw new TempHandler(ErrorStatus.TEMP_EXCEPTION);
    }

}
