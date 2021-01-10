package com.android.mytaskseat.Network;

import com.android.mytaskseat.Model.GetSeatsResponse;
import com.android.mytaskseat.Model.RequestModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {


    @POST("Gasco/seat-map.php/")
    @FormUrlEncoded
    Call<GetSeatsResponse> getSeats(@Query("key") String mKeyValue,@FieldMap Map<String, String> params);
}
