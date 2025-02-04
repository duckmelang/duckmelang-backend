package umc.duckmelang.domain.chatmessage.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_message")  // collection = "실제 mongoDB 내 컬렉션 이름")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatMessage {

    // MongoDB의 기본 _id 필드인 ObjectId를 활용하면 별도의 ID 생성 로직 없이 MongoDB가 자동으로 고유 ID를 생성해준다.
    // @Id 필드의 타입을 String 또는 ObjectId로 설정하면 된다.
    // 기본 _id 필드는 같은 초에 생성된 ObjectId들 간의 순서도 보장해준다. (초단위까지 같은 시각에 여러 채팅이 와도 정렬 가능)
    // _id는 67a1dba3a313403f41a4e2a5으로 저장되어서 시간 순으로 정렬이 될지 의심스러울 수 있겠지만 .sort 메서드를 통해 정렬이 가능하다고 한다.
    @Id
    private String id;

    private Long senderId;   // 메세지를 보낸 사람

    private Long receiverId;   // 메세지를 받은 사람

    private Long chatRoomId;   // 대화가 이루어지고 있는 채팅방 ID

    private String content;   // 메세지 내용

    @CreatedDate
    private LocalDateTime createdAt;

}