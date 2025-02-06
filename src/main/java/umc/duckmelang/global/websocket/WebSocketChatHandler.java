package umc.duckmelang.global.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public class WebSocketChatHandler extends TextWebSocketHandler {

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
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // payload를 로그 메세지로 출력한다.
        String payload = message.getPayload();
        log.info("payload {}", payload);

        // 세션값들을 반복문으로 순회하고, 동일한 아이디가 아니면 메시지를 발신한다.
        clientSessions.forEach((key, value) -> {
            //key-value 확인용
            log.info("key :: {}  value :: {}", key, value);

            if (!key.equals(session.getId())) {
                try {
                    value.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // WebSocket이 종료되면 호출되는 메서드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        // 연결이 종료되었다는 로그 메세지를 출력한다.
        log.info("WebSocket 연결이 종료되었습니다. session Id: {}", session.getId());

        // 해당 세션을 WebSocket 세션 관리 리스트에서 제거한다.
        clientSessions.remove(session);

        // 연결 종료 메서드를 클라이언트에게도 전달한다.
        session.sendMessage(new TextMessage("WebSocket 연결 종료"));
    }


    // WebSocket 전송 오류를 처리하는 메서드
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("WebSocket 전송 오류: session Id: {}, 원인: {}", session.getId(), exception.getMessage());
    }

}
