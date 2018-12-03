package com.ging.chat.api;

import com.ging.chat.config.Define;

public class ApiUtils {

    private static final String BASE_URL = Define.API.BASE_URL;

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

}
