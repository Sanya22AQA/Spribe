package client;

import lombok.SneakyThrows;
import models.*;
import org.aeonbits.owner.ConfigFactory;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlayerClientFacade {
    private final PlayerConfig config = ConfigFactory.create(PlayerConfig.class);
    private final PlayerClient playerClient;

    public PlayerClientFacade() {
        this.playerClient = PlayerFactory.create(PlayerClient.class, config.baseUrl());
    }

    @SneakyThrows
    public Response<PlayerGetAllResponseDto> getAllPlayers() {
        return playerClient
                .getAllPlayers()
                .execute();
    }

    @SneakyThrows
    public Response<PlayerWithIdAndPasswordDto> getPlayerById(Integer playerId) {
        return playerClient
                .getPlayerById(Map.of("playerId", playerId))
                .execute();
    }

    @SneakyThrows
    public Response<PlayerWithIdAndPasswordDto> createPlayer(String editor, PlayerWithPasswordDto player) {
        var params = new HashMap<String, Object>();
        params.put("age", player.getAge());
        params.put("login", player.getLogin());
        params.put("screenName", player.getScreenName());
        params.put("role", player.getRole().name().toLowerCase());
        params.put("gender", player.getGender().name().toLowerCase());
        if (!Objects.isNull(player.getPassword())) {
            params.put("password", player.getPassword());
        }
        return playerClient
                .createPlayer(editor, params)
                .execute();
    }

    @SneakyThrows
    public Response<PlayerWithIdDto> updatePlayer(String editor, Integer id, PlayerWithPasswordDto player) {
        return playerClient
                .updatePlayer(editor, id, player)
                .execute();
    }

    @SneakyThrows
    public Response<Void> deletePlayer(String editor, Integer playerId) {
        return playerClient
                .deletePlayer(editor, Map.of("playerId", playerId))
                .execute();
    }
}
