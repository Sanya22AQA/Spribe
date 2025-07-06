package util;

import enums.Gender;

public class GenderDeserializer extends LowerCaseEnumDeserializer<Gender> {
    public GenderDeserializer() {
        super(Gender.class);
    }
}
