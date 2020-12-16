package NetworkClient;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    public static String BASE_URL = "https://api.yelp.com";
    public static String API_KEY = "BUwGGrfFR9-3PXAlidnjIpl3ChXtmaIb20XmbRjNM_AGGXHUrTPHmn0UolW-LD-TWouPadXq92WYujVKyFK2-BORrSGr_bBja5ONZdpseJYeETitmS38FTAkZpiyX3Yx";
    private static  int timeOut = 30;
    private static NetworkClient mInstance;

    private OkHttpClient getHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("Authorization"," Bearer "+API_KEY).build();
                        return chain.proceed(request);
                    }
                })
                .build();
    }
    public static synchronized NetworkClient getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkClient();
        }
        return mInstance;
    }
    public ApiService getClient() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHttpClient())
                .build()
                .create(ApiService.class);
    }
}