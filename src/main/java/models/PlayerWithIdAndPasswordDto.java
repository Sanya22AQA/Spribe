package models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 *  PlayerCreateResponseDto, PlayerGetByPlayerIdResponseDto
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerWithIdAndPasswordDto extends PlayerBaseDto {
    Integer id;
    String password;
}

