package com.teamalpha.bloodpals.data.bloodbanks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BloodBanksApi {

    @GET("api/action/datastore_search")
    Call<BloodBanksApiResponse> getInformationOnBloodBanks(@Query("resource_id") String resourceId, @Query("query") String query, @Query("limit") String limit);

}
