package umc.duckmelang.global.temp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.duckmelang.global.apipayload.code.status.ErrorStatus;
import umc.duckmelang.global.apipayload.exception.TempException;

@Service
@RequiredArgsConstructor
public class TempQueryServiceImpl implements TempQueryService {

    @Override
    public void CheckFlag(Integer flag){
        if(flag == 1)
            throw new TempException(ErrorStatus.TEMP_EXCEPTION);
    }

}
