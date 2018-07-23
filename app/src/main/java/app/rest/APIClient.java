package app.rest;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fadejimi on 7/4/18.
 */

public class APIClient {
    private static Retrofit retrofit = null;
    private static String baseUrl = "https://task-schedular-1990.herokuapp.com/api/v1/";
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static HttpLoggingInterceptor logger = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static<S> S createService(Class<S> serviceClass, String token) {
        if (!TextUtils.isEmpty(token)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(token);
            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.readTimeout(1, TimeUnit.MINUTES)
                        .addInterceptor(interceptor)
                        .addInterceptor(logger);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }

    /**static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }**/
}
