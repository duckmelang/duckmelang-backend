package umc.duckmelang.global.temp.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.duckmelang.global.apipayload.ApiResponse;
import umc.duckmelang.global.temp.converter.TempConverter;
import umc.duckmelang.global.temp.dto.TempResponse;
import umc.duckmelang.global.temp.service.TempQueryService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/temp")
@RequiredArgsConstructor
public class TempRestController {

    private final TempQueryService tempQueryService;

    @GetMapping("/test")
    @Operation(summary = "테스트 API",description = "테스트입니다")
    public ApiResponse<TempResponse.TempTestDTO> testAPI(){

        return ApiResponse.onSuccess(TempConverter.toTempTestDTO());
    }

    @GetMapping("/exception")
    public ApiResponse<TempResponse.TempExceptionDTO> exceptionAPI(@RequestParam Integer flag){
        tempQueryService.CheckFlag(flag);
        return ApiResponse.onSuccess(TempConverter.toTempExceptionDTO(flag));
    }

    @Operation(summary = "Authorization 확인 API" , description = "Authorization 헤더에 넣은 값이 제대로 컨트롤러 내부에 도달하는 지 확인")
    @GetMapping("/authorization")
    public ResponseEntity<Object> authHeaderCheck(HttpServletRequest request){
        Map<String, String> response = new HashMap<>(){{
            put("Authorization", request.getHeader("Authorization"));
        }};
        return ResponseEntity.ok(response);
    }
}
