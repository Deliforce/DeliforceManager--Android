package com.app.deliforcemanager.ServiceApi;

import android.net.Uri;

import com.app.deliforcemanager.BuildConfig;
import com.app.deliforcemanager.ModelClass.MyQueryParams;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class APIServiceFactory {

    //    public static final String BASE_URL = "https://jqddrk2828.execute-api.ap-south-1.amazonaws.com/Development/";
//    public static final String BASE_URL = "https://fluay3gbph.execute-api.ap-south-1.amazonaws.com/Development/";
//    public static final String BASE_URL = "https://za0d3rbfo2.execute-api.ap-south-1.amazonaws.com/Development/";
    /**
     * live base url
     */
    private static final String BASE_URL = "https://2ocwnhz66m.execute-api.ap-south-1.amazonaws.com/Development/manager/";

//    private static final String BASE_URL = "https://za0d3rbfo2.execute-api.ap-south-1.amazonaws.com/Development/";// development url

    //    public static final String BASE_URL = "https://za0d3rbfo2.execute-api.ap-south-1.amazonaws.com/Development/";
    //public static final String MAIN_URL = "http://192.168.1.38:3000/auth/v1/l";

    private static final String CACHE_CONTROL = "Cache-Control";

    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(1, TimeUnit.MINUTES).connectTimeout(1, TimeUnit.MINUTES);

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        });

        if (BuildConfig.DEBUG)
            httpClient.interceptors().add(logging);
        OkHttpClient OkHttpClient = httpClient.build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient)
                .build();
    }




    /*private static OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .addInterceptor(provideOfflineCacheInterceptor())
                //.addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache());
        if (BuildConfig.DEBUG)
            httpClient.interceptors().add(logging);
        httpClient.networkInterceptors().add(provideCacheInterceptor());
        return httpClient.build();
    }

    private static Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(DeliForce.getInstance().getCacheDir(), "responses"),
                    10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
            Timber.e("Could not create Cache!");
        }
        return cache;
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(message -> Timber.e(message));
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HEADERS : NONE);
        return httpLoggingInterceptor;
    }*/

    private static Interceptor provideCacheInterceptor() {
        return chain -> {
            Response originalResponse = chain.proceed(chain.request());
            /*if (DeliForce.hasNetwork()) {
                int maxAge = 60; // read from cache for 1 minute
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }*/

            /*Response response = chain.proceed(chain.request());

            // re-write response header to force use of cache
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(2, TimeUnit.MINUTES)
                    .build();

            return response.newBuilder()
                    .header(CACHE_CONTROL, cacheControl.toString())
                    .header("Accept", "application/json")
                    .build();*/
            return originalResponse;
        };
    }

    private static Interceptor provideOfflineCacheInterceptor() {
        return chain -> {
            Request request = chain.request();

            /*if (!DeliForce.hasNetwork()) {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .header("Accept", "application/json")
                        .method(request.method(), request.body())
                        .cacheControl(cacheControl)
                        .build();
            }*/

            return chain.proceed(request);
        };
    }
}

