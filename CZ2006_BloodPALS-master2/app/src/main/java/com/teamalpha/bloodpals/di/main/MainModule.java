package com.teamalpha.bloodpals.di.main;

import com.teamalpha.bloodpals.data.bloodbanks.BloodBanksApiService;
import com.teamalpha.bloodpals.logic.qrcode.QRCodeGeneratorApiService;
import com.teamalpha.bloodpals.logic.bloodbanks.BloodBanksManager;
import com.teamalpha.bloodpals.presentation.bloodbanks.BloodBankRecyclerAdapter;
import com.teamalpha.bloodpals.presentation.mydonationactivities.MyDonationActivitiesRecyclerAdapter;
import com.teamalpha.bloodpals.presentation.sample.RewardRecyclerAdapter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MainModule {

    @MainScope
    @Provides
    public static BloodBanksApiService provideBloodBanksService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://data.gov.sg/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return new BloodBanksApiService(retrofit);
    }

    @MainScope
    @Provides
    static BloodBanksManager provideBloodBanksManager(BloodBanksApiService bloodBanksApiService){
        return new BloodBanksManager(bloodBanksApiService);
    }

    @Provides
    static BloodBankRecyclerAdapter provideBloodBankRecyclerAdapter(){
        return new BloodBankRecyclerAdapter();
    }

    @MainScope
    @Provides
    static RewardRecyclerAdapter provideRewardRecyclerAdapter(){
        return new RewardRecyclerAdapter();
    }

    @MainScope
    @Provides
    public static QRCodeGeneratorApiService provideQRCodeGeneratorApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.qrserver.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return new QRCodeGeneratorApiService(retrofit);
    }

    @Provides
    static MyDonationActivitiesRecyclerAdapter provideMyDonationActivitiesRecyclerAdapter(){
        return new MyDonationActivitiesRecyclerAdapter();
    }

}
