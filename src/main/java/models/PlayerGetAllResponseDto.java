package models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import enums.Gender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import util.GenderDeserializer;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerGetAllResponseDto {
    List<PlayersItemDto> players;

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PlayersItemDto {
        Integer id;
        Integer age;
        @JsonDeserialize(using = GenderDeserializer.class)
        Gender gender;
        String screenName;
    }
}
