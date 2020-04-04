package com.teamalpha.bloodpals.presentation.myprofile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.teamalpha.bloodpals.logic.qrcode.QRCodeGeneratorApiService;
import com.teamalpha.bloodpals.logic.session.SessionManager;
import com.teamalpha.bloodpals.presentation.utils.SingleLiveEvent;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.TextStringResource;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyQRCodeViewModel extends ViewModel {

    private static final String TAG = "MyQRCodeViewModel";

    private final SingleLiveEvent<StringResource> toastMessageLiveData = new SingleLiveEvent<>();
    private final MutableLiveData<ResponseBody> createQRCodeResponseBodyLiveData = new MutableLiveData<>();

    private SessionManager sessionManager;
    private QRCodeGeneratorApiService qrCodeGeneratorApiService;

    @Inject
    public MyQRCodeViewModel(SessionManager sessionManager, QRCodeGeneratorApiService qrCodeGeneratorApiService) {
        this.sessionManager = sessionManager;
        this.qrCodeGeneratorApiService = qrCodeGeneratorApiService;

        Log.d(TAG, "MyQRCodeViewModel: view model is ready...");
    }

    public LiveData<StringResource> observeToastMessage() {
        return toastMessageLiveData;
    }

    public LiveData<ResponseBody> observeCreateQRCodeResponseBody() {
        return createQRCodeResponseBodyLiveData;
    }

    public void createQRCode() {
        Call<ResponseBody> call = qrCodeGeneratorApiService.createQRCode(sessionManager.getUser().getValue().data.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    createQRCodeResponseBodyLiveData.setValue(response.body());
                }
                else {
                    Log.e(TAG, "loadData: ERROR - "+response.errorBody().toString());
                    createQRCodeResponseBodyLiveData.setValue(null);
                    toastMessageLiveData.setValue(new TextStringResource(response.errorBody().toString()));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "loadData: ERROR - "+t.getMessage());
                createQRCodeResponseBodyLiveData.setValue(null);
                toastMessageLiveData.setValue(new TextStringResource(t.getMessage()));
            }
        });
    }

}
