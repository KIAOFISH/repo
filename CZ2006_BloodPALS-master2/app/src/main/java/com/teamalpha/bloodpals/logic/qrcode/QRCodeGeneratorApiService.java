package com.teamalpha.bloodpals.logic.qrcode;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class QRCodeGeneratorApiService {

    private static final String TAG = "QRCodeGeneratorApiService";

    private static final String IMAGE_SIZE = "1000x1000";

    private Retrofit retrofit;

    private QRCodeGeneratorApi qrCodeGeneratorApi;

    @Inject
    public QRCodeGeneratorApiService(Retrofit retrofit) {
        this.retrofit = retrofit;
        qrCodeGeneratorApi = retrofit.create(QRCodeGeneratorApi.class);
    }

    public Call<ResponseBody> createQRCode(String code) {
        return qrCodeGeneratorApi.createQRCode(IMAGE_SIZE, code);
    }

}
