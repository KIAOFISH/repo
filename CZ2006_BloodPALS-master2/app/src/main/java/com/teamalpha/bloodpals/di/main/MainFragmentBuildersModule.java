package com.teamalpha.bloodpals.di.main;

import com.teamalpha.bloodpals.presentation.bloodbanks.BloodBanksFragment;
import com.teamalpha.bloodpals.presentation.home.HomeFragment;
import com.teamalpha.bloodpals.presentation.login.LoginFragment;
import com.teamalpha.bloodpals.presentation.mydonationactivities.CreateDonationActivityFragment;
import com.teamalpha.bloodpals.presentation.mydonationactivities.EditDonationActivityFragment;
import com.teamalpha.bloodpals.presentation.mydonationactivities.MyDonationActivitiesFragment;
import com.teamalpha.bloodpals.presentation.myprofile.EditMyProfileFragment;
import com.teamalpha.bloodpals.presentation.myprofile.MyProfileFragment;
import com.teamalpha.bloodpals.presentation.myprofile.MyQRCodeFragment;
import com.teamalpha.bloodpals.presentation.register.RegisterFragment;
import com.teamalpha.bloodpals.presentation.sample.RewardFragment;
import com.teamalpha.bloodpals.presentation.userprofile.ScanQRCodeFragment;
import com.teamalpha.bloodpals.presentation.userprofile.UserProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract HomeFragment contributeHomeFragment();

    @ContributesAndroidInjector
    abstract LoginFragment contributeLoginFragment();

    @ContributesAndroidInjector
    abstract RegisterFragment contributeRegisterFragment();

    @ContributesAndroidInjector
    abstract MyProfileFragment contributeMyProfileFragment();

    @ContributesAndroidInjector
    abstract EditMyProfileFragment contributeEditMyProfileFragment();

    @ContributesAndroidInjector
    abstract MyQRCodeFragment contributeMyQRCodeFragment();

    @ContributesAndroidInjector
    abstract MyDonationActivitiesFragment contributeMyDonationActivitiesFragment();

    @ContributesAndroidInjector
    abstract CreateDonationActivityFragment contributeCreateDonationActivityFragment();

    @ContributesAndroidInjector
    abstract EditDonationActivityFragment contributeEditDonationActivityFragment();

    @ContributesAndroidInjector
    abstract BloodBanksFragment contributeBloodBanksFragment();

    @ContributesAndroidInjector
    abstract ScanQRCodeFragment contributeScanQRCodeFragment();

    @ContributesAndroidInjector
    abstract UserProfileFragment contributeUserProfileFragment();

    @ContributesAndroidInjector
    abstract RewardFragment contributeRewardFragment();

}
