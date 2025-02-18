package umc.duckmelang.mongo.chatmessage.domain.enums;

public enum MessageType {
    TEXT("텍스트"),
    IMAGE("이미지"),
    FILE("파일"),
    LINK("링크");

    private final String description;

    MessageType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        if (this == TEXT) {
            return ""; // TEXT는 content 그대로 사용하기 위해 빈 문자열 반환
        }
        return this.description + "(을)를 보냈습니다.";
    }
}