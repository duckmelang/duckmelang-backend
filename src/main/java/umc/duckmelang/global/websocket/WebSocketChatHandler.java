package umc.duckmelang.global.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import umc.duckmelang.mongo.chatmessage.converter.ChatMessageConverter;
import umc.duckmelang.mongo.chatmessage.domain.ChatMessage;
import umc.duckmelang.mongo.chatmessage.dto.ChatMessageRequestDto;
import umc.duckmelang.mongo.chatmessage.service.ChatMessageCommandService;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ChatMessageCommandService chatMessageCommandService;
    private final ObjectMapper objectMapper;

    // WebSocket 세션 관리 리스트
    private final ConcurrentHashMap<String, WebSocketSession> clientSessions = new ConcurrentHashMap<>();


    // WebSocket 연결에 성공하여 WebSocket을 사용할 준비가 되면 호출되는 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 성공했다는 로그 메세지를 출력한다.
        log.info("WebSocket 연결에 성공했습니다. session Id: {}", session.getId());

        // 해당 세션을 WebSocket 세션 관리 리스트에 추가한다.
        clientSessions.put(session.getId(), session);  //session.getId()는 각 세션의 고유 ID를 출력한다.

        // 연결 성공 메세지를 클라이언트에게도 전달한다.
        session.sendMessage(new TextMessage("WebSocket 연결 완료"));
    }


    // 새로운 WebSocket 메세지가 발생하면 처리하는 메서드
    // = 채팅 메세지 전송 API
    @Operation(summary = "WebSocket 연결 안내",
            description = "WebSocket을 통해 실시간 채팅 기능을 사용합니다.(WebSocket은 Swagger에서 지원하지 않아 swagger를 통한 테스트는 어렵습니다. 클라이언트는 ws://(도메인명)/ws/chat 엔드포인트로 연결합니다.")
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        // 1. 클라이언트로부터 메세지를 수신받는다.
        String payload = message.getPayload();
        ChatMessageRequestDto.CreateChatMessageDto request =
                objectMapper.readValue(payload, ChatMessageRequestDto.CreateChatMessageDto.class);

            // payload를 로그 메세지로 출력한다.
        log.info("payload {}", payload);


        // 2. 메세지를 처리한다.
            // service 로직을 통하여 메세지를 저장한다.
            // 채팅방이 없는 경우 채팅방을 생성한다.(첫 메시지의 경우가 여기에 해당한다.)
        ChatMessage savedChatMessage = chatMessageCommandService.processMessage(request);

        // 3. 응답 메시지를 생성한다.
        TextMessage responseMessage = ChatMessageConverter.toTextMessage(savedChatMessage);

        // 4. 클라이언트로 응답을 전송한다.
            // 세션값들을 반복문으로 순회하고, 동일한 아이디가 아니면 메시지를 발신한다.
        clientSessions.forEach((key, value) -> {
            log.info("key :: {}  value :: {}", key, value);       // key-value 확인용 로그

            if (!key.equals(session.getId())) {  // 현재 세션과 다른 세션에만 메시지 전송 (현재 메시지를 보낸 클라이언트를 제외한 나머지 클라이언트들에게만 메시지를 전송)
                try {
                    // 세션이 열려 있는지 확인
                    if (value.isOpen()) {
                        value.sendMessage(message);
                    } else {
                        log.warn("세션이 닫혀 있습니다: session Id = {}", key);
                    }
                } catch (IOException e) {
                    log.error("메시지 전송 오류: session Id = {}", key, e);
                }
            }
        });
    }


    // WebSocket이 종료되면 호출되는 메서드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // 연결이 종료되었다는 로그 메세지를 출력한다.
        log.info("WebSocket 연결이 종료되었습니다. session Id: {}", session.getId());

        // 해당 세션을 WebSocket 세션 관리 리스트에서 제거한다.
        clientSessions.remove(session.getId());
    }


    // WebSocket 전송 오류를 처리하는 메서드
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("WebSocket 전송 오류: session Id: {}, 원인: {}", session.getId(), exception.getMessage());
    }
}
