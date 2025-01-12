package umc.duckmelang.jwt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Tag(name = "JWT Test API", description = "JWT 테스트용 API")
public class JwtTestController {
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "JWT 생성", description = "JWT Access Token과 Refresh Token을 생성합니다.")
    @GetMapping("/generate-token")
    public ResponseEntity<JwtToken> generateToken() {
        Authentication authentication = jwtTokenProvider.getAuthentication("testuser");
        JwtToken token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "JWT 검증", description = "JWT Access Token을 검증합니다.")
    @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
        System.out.println("Received Authorization Header: " + token);
        String actualToken = token.replace("Bearer ", "");
        if (jwtTokenProvider.validateToken(actualToken)) {
            String userId = jwtTokenProvider.getUserId(actualToken);
            return ResponseEntity.ok("Valid Token for User ID: " + userId);
        } else {
            return ResponseEntity.badRequest().body("Invalid Token");
        }
    }
}
