package com.android.mytaskseat.Network;

import com.android.mytaskseat.Model.GetSeatsResponse;
import com.android.mytaskseat.Model.RequestModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("Gasco/seat-map.php/")
    Call<GetSeatsResponse> getSeats(@Query("key") String key, @Body RequestModel requestModel);
}
