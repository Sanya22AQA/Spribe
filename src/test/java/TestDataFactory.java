import models.PlayerWithPasswordDto;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static enums.Gender.FEMALE;
import static enums.Gender.MALE;
import static enums.Role.ADMIN;
import static enums.Role.USER;

public class TestDataFactory {
    private static final String auto = "Auto";

    private TestDataFactory() {
    }

    public static PlayerWithPasswordDto getDefaultPlayer() {
        var age = generateRandomInteger(17, 60);
        var password = generateRandomPassword(7, 15);
        var screenName = auto.concat(generateRandomAlphabetic(10, 20));
        var login = auto.concat(generateRandomAlphabetic(10, 20));
        var role = getRandomFromList(List.of(ADMIN, USER));
        var gender = getRandomFromList(List.of(MALE, FEMALE));
        return PlayerWithPasswordDto.builder().age(age).password(password).login(login).screenName(screenName).role(role).gender(gender).build();
    }

    public static Integer generateRandomInteger(int minValue, int maxValue) {
        return ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
    }

    public static String generateRandomAlphabetic(int min, int max) {
        int len = ThreadLocalRandom.current().nextInt(min, max + 1);
        return RandomStringUtils.randomAlphabetic(len);
    }

    public static String generateRandomPassword(int min, int max) {
        int len = ThreadLocalRandom.current().nextInt(min, max + 1);
        return Stream.generate(() -> RandomStringUtils.randomAlphanumeric(len))
                .filter(s -> s.matches(".*[A-Za-z].*") && s.matches(".*\\d.*"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Password could not be generated"));
    }

    public static <T> T getRandomFromList(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List is null or empty");
        }
        int index = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(index);
    }
}
