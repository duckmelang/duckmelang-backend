package umc.duckmelang.domain.eventcategory.domain.enums;

public enum EventKind {
    EVENT("행사"),
    PERFORMANCE("공연");

    private final String name;

    private EventKind(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
