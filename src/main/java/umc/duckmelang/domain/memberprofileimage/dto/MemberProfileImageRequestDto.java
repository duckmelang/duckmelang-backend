package umc.duckmelang.domain.memberprofileimage.dto;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberProfileImageRequestDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberProfileImageDto {
        private Long imageId;

        private boolean isPublic;

        @JsonGetter("isPublic") // JSON 직렬화 시 "isPublic"으로 설정
        public boolean isPublic() {
            return isPublic;
        }
    }
}
