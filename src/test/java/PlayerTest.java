import client.PlayerClientFacade;
import client.PlayerConfig;
import enums.Role;
import io.qameta.allure.Issue;
import models.PlayerWithIdAndPasswordDto;
import models.PlayerWithPasswordDto;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {
    private static final Logger log = LogManager.getLogger(PlayerTest.class);
    private final PlayerClientFacade facade = new PlayerClientFacade();
    private final PlayerConfig config = ConfigFactory.create(PlayerConfig.class);
    private final String loginOfSupervisor = config.loginOfSupervisor();

    @Test
    public void verifyGetAllPlayers() {
        log.info("Start of preconditions");
        var player = getCreatedNewPlayer();
        log.info("End of preconditions");

        var response = facade.getAllPlayers();
        log.info("Response code of GET all method = {}", response.code());
        assertThat(response.isSuccessful())
                .as("Response should be successful")
                .isTrue();

        var optionalPlayerItem = response.body().getPlayers().stream().filter(p -> p.getId().equals(player.getId())).findFirst();
        assertThat(optionalPlayerItem)
                .as("Created player must be present in get all list")
                .isPresent();
        log.info("Created player found in the list");
    }

    @Test
    public void verifyGetPlayerById() {
        log.info("Start of preconditions");
        var player = getCreatedNewPlayer();
        log.info("End of preconditions");

        var id = player.getId();
        var response = facade.getPlayerById(id);
        log.info("Response code of GET by id method = {}", response.code());
        assertThat(response.isSuccessful())
                .as("Response should be successful")
                .isTrue();

        var actualLogin = response.body().getLogin();
        assertThat(actualLogin).as("Login value should be the same").isEqualTo(player.getLogin());
        log.info("Login values are the same");
    }

    @Test
    public void verifyCreatePlayer() {
        var player = TestDataFactory.getDefaultPlayer();
        var response = facade.createPlayer(loginOfSupervisor, player);
        log.info("Response code of Create player method = {}", response.code());
        assertThat(response.isSuccessful())
                .as("Response should be successful")
                .isTrue();

        var actualLogin = response.body().getLogin();
        assertThat(actualLogin).as("Login value should be the same").isEqualTo(player.getLogin());
        log.info("Login values are the same");
    }

    @Issue("BUG-6")
    @Test(dataProvider = "invalidPasswords")
    public void verifyCreatePlayerWithInvalidPasswords(String invalidPassword) {
        log.debug("Invalid password={}", invalidPassword);
        var player = TestDataFactory.getDefaultPlayer().toBuilder().password(invalidPassword).build();
        var response = facade.createPlayer(loginOfSupervisor, player);
        log.info("Response code of Create player method = {}", response.code());
        assertThat(response.isSuccessful())
                .as("Response should not be successful")
                .isFalse();
    }

    @Test
    public void verifyDeletePlayer() {
        log.info("Start of preconditions");
        var player = getCreatedNewPlayer();
        log.info("End of preconditions");

        var id = player.getId();
        var response = facade.deletePlayer(loginOfSupervisor, id);
        log.info("Response code of Delete player method = {}", response.code());
        assertThat(response.isSuccessful())
                .as("Response should be successful")
                .isTrue();

        var optionalPlayerItem = facade.getAllPlayers().body().getPlayers().stream().filter(p -> p.getId().equals(player.getId())).findFirst();
        assertThat(optionalPlayerItem)
                .as("Created player must be present in get all list")
                .isEmpty();
        log.info("Deleted player is not found in the list");
    }

    @Test
    public void verifyDeletePlayerWithNonExistentId() {
        var nonExistentId = facade.getAllPlayers().body().getPlayers().stream().mapToInt(playersItemDto -> playersItemDto.getId()).max().orElseThrow() + 1;
        log.info("Non existent id ={}", nonExistentId);
        var response = facade.deletePlayer(loginOfSupervisor, nonExistentId);
        log.info("Response code of Delete player method = {}", response.code());
        assertThat(response.isSuccessful())
                .as("Response should not be successful")
                .isFalse();
    }

    @Test
    public void verifyUpdatePlayer() {
        log.info("Start of preconditions");
        var player = getCreatedNewPlayer();
        log.info("End of preconditions");

        var id = player.getId();
        var newPlayer = TestDataFactory.getDefaultPlayer();
        var response = facade.updatePlayer(loginOfSupervisor, id, newPlayer);
        log.info("Response code of Update player method = {}", response.code());
        assertThat(response.isSuccessful())
                .as("Response should be successful")
                .isTrue();

        var updatedPlayer = response.body();
        assertThat(updatedPlayer).usingRecursiveComparison().ignoringFields("id", "role").isEqualTo(newPlayer); //Role is ignored due to bug(it will be covered in next TC)
        var actualPassword = facade.getPlayerById(id).body().getPassword();
        assertThat(actualPassword).isEqualTo(newPlayer.getPassword());
        log.info("All fields were updated");
    }

    @Issue("BUG-1")
    @Test
    public void verifyUpdateRoleOfPlayer() {
        log.info("Start of preconditions");
        var player = getCreatedNewPlayer();
        log.info("End of preconditions");

        var id = player.getId();
        var currentRole = facade.getPlayerById(id).body().getRole();
        log.info("Current role {}", currentRole);
        var newRole = currentRole == Role.ADMIN ? Role.USER : Role.ADMIN;
        var response = facade.updatePlayer(loginOfSupervisor, id, PlayerWithPasswordDto.builder().role(newRole).build());
        log.info("Response code of Update player method = {}", response.code());
        assertThat(response.isSuccessful())
                .as("Response should be successful")
                .isTrue();
        assertThat(response.body().getRole()).isEqualTo(newRole);
    }

    @Issue("BUG-2")
    @Test(dataProvider = "invalidAge")
    public void verifyUpdatePlayerWithInvalidAge(Integer invalidAge) {
        log.info("Start of preconditions");
        var player = getCreatedNewPlayer();
        log.info("End of preconditions");

        log.debug("Invalid age={}", invalidAge);
        var response = facade.updatePlayer(loginOfSupervisor, player.getId(), PlayerWithPasswordDto.builder().age(invalidAge).build());
        log.info("Response code of Update player method = {}", response.code());
        assertThat(response.isSuccessful())
                .as("Response should not be successful")
                .isFalse();
    }

    private PlayerWithIdAndPasswordDto getCreatedNewPlayer() {
        var player = TestDataFactory.getDefaultPlayer();
        var response = facade.createPlayer(loginOfSupervisor, player);
        if (response.isSuccessful()) {
            log.info("Player is created");
        } else {
            log.error("User is not created: status={}, body={}", response.code(), response.body());
            Assert.fail("Failed to create player");
        }
        return response.body();
    }

    @DataProvider
    private Object[][] invalidPasswords() {
        return new Object[][]{
                {"!@#$"},
                {TestDataFactory.generateRandomPassword(1, 6)},
                {TestDataFactory.generateRandomPassword(16, 25)},
                {TestDataFactory.generateRandomInteger(7, 15).toString()},
                {TestDataFactory.generateRandomAlphabetic(7, 15)}
        };
    }

    @DataProvider
    private Object[][] invalidAge() {
        return new Object[][]{
                {16},
                {61},
                {-5}
        };
    }
}
