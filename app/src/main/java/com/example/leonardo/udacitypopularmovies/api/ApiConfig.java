package com.example.leonardo.udacitypopularmovies.api;


import com.example.leonardo.udacitypopularmovies.App;
import com.example.leonardo.udacitypopularmovies.R;

public final class ApiConfig {

    private static ApiConfig instance;

    public static ApiConfig getInstance() {
        if(instance == null){
            instance = new ApiConfig();
        }
        return instance;
    }

    private ApiConfig(){}

    public String getApiKey(){
        return App.getInstance().getString(R.string.tmdb_api_key);
    }
}
