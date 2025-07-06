package client;

import io.qameta.allure.okhttp3.AllureOkHttp3;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

public class PlayerFactory {
    private static final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS);

    static {
        var logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(new AllureOkHttp3()).addInterceptor(logging);
    }

    public static <T> T create(Class<T> clientInterface, String url) {
        var client = clientBuilder.build();
        return build(client, clientInterface, url);
    }

    private static <T> T build(OkHttpClient client, Class<T> classInterface, String url) {
        var retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(url).client(client).addConverterFactory(JacksonConverterFactory.create());
        return retrofitBuilder.build().create(classInterface);
    }
}
