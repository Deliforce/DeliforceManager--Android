package com.app.deliforcemanager.Security;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Get reuqest info
        // Create modified request to return
        Request modRequest = chain.request();
        Response response = chain.proceed(modRequest);
        // your logic...

        return response;
    }
}