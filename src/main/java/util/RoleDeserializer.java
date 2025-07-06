package util;

import enums.Role;

public class RoleDeserializer extends LowerCaseEnumDeserializer<Role> {
    public RoleDeserializer() {
        super(Role.class);
    }
}
