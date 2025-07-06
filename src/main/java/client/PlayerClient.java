package client;

import models.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface PlayerClient {

    @GET("player/get/all")
    Call<PlayerGetAllResponseDto> getAllPlayers();

    @POST("player/get")
    Call<PlayerWithIdAndPasswordDto> getPlayerById(@Body Map<String, Integer> params);

    @GET("player/create/{editor}")
    Call<PlayerWithIdAndPasswordDto> createPlayer(@Path("editor") String editor, @QueryMap Map<String, Object> params);

    @PATCH("player/update/{editor}/{id}")
    Call<PlayerWithIdDto> updatePlayer(@Path("editor") String editor, @Path("id") Integer id, @Body PlayerWithPasswordDto player);

    @HTTP(method = "DELETE", path = "player/delete/{editor}", hasBody = true)
    Call<Void> deletePlayer(@Path("editor") String editor, @Body Map<String, Integer> params);
}
