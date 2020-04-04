package com.teamalpha.bloodpals.logic.qrcode;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QRCodeGeneratorApi {

    /**
     * Create the QR Code
     * @param size: size of the qr code bitmap to generate (e.g. 1000x1000)
     * @param data:
     * @return
     */
    @GET("v1/create-qr-code")
    Call<ResponseBody> createQRCode(@Query("size") String size, @Query("data") String data);

}
