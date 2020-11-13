package es.upm.miw.windspeed;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface ICountryRESTAPIService {

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("/data/2.5/forecast?id=3117735&appid=72fd3252f9a160f18dfa0e4ee89a92d8")
    Call<List<Forecast>> getAllForecast();
    // https://restcountries.eu/rest/v2/alpha/ES

    @GET("/data/2.5/forecast?id=3117735&appid=72fd3252f9a160f18dfa0e4ee89a92d8")
    Call<Forecast> getForecast();

}

