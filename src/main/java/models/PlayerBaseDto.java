package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import enums.Gender;
import enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import util.GenderDeserializer;
import util.LowerCaseEnumSerializer;
import util.RoleDeserializer;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerBaseDto {
    Integer age;
    String screenName;
    String login;
    @JsonSerialize(using = LowerCaseEnumSerializer.class)
    @JsonDeserialize(using = RoleDeserializer.class)
    Role role;
    @JsonSerialize(using = LowerCaseEnumSerializer.class)
    @JsonDeserialize(using = GenderDeserializer.class)
    Gender gender;
}
