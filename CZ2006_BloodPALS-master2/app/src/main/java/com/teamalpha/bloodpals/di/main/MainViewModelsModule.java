package com.teamalpha.bloodpals.di.main;

import androidx.lifecycle.ViewModel;

import com.teamalpha.bloodpals.di.ViewModelKey;
import com.teamalpha.bloodpals.presentation.MainViewModel;
import com.teamalpha.bloodpals.presentation.bloodbanks.BloodBanksViewModel;
import com.teamalpha.bloodpals.presentation.home.HomeViewModel;
import com.teamalpha.bloodpals.presentation.login.LoginViewModel;
import com.teamalpha.bloodpals.presentation.mydonationactivities.CreateDonationActivityViewModel;
import com.teamalpha.bloodpals.presentation.mydonationactivities.EditDonationActivityViewModel;
import com.teamalpha.bloodpals.presentation.mydonationactivities.MyDonationActivitiesViewModel;
import com.teamalpha.bloodpals.presentation.myprofile.EditMyProfileViewModel;
import com.teamalpha.bloodpals.presentation.myprofile.MyProfileViewModel;
import com.teamalpha.bloodpals.presentation.myprofile.MyQRCodeViewModel;
import com.teamalpha.bloodpals.presentation.register.RegisterViewModel;
import com.teamalpha.bloodpals.presentation.sample.RewardViewModel;
import com.teamalpha.bloodpals.presentation.userprofile.ScanQRCodeViewModel;
import com.teamalpha.bloodpals.presentation.userprofile.UserProfileViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    public abstract ViewModel bindHomeViewModel(HomeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    public abstract ViewModel bindLoginViewModel(LoginViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    public abstract ViewModel bindRegisterViewModel(RegisterViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyProfileViewModel.class)
    public abstract ViewModel bindMyProfileViewModel(MyProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EditMyProfileViewModel.class)
    public abstract ViewModel bindEditMyProfileViewModel(EditMyProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyQRCodeViewModel.class)
    public abstract ViewModel bindMyQRCodeViewModel(MyQRCodeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyDonationActivitiesViewModel.class)
    public abstract ViewModel bindMyDonationActivitiesViewModel(MyDonationActivitiesViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CreateDonationActivityViewModel.class)
    public abstract ViewModel bindCreateDonationActivityFragment(CreateDonationActivityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EditDonationActivityViewModel.class)
    public abstract ViewModel bindEditDonationActivityFragment(EditDonationActivityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BloodBanksViewModel.class)
    public abstract ViewModel bindBloodBanksViewModel(BloodBanksViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ScanQRCodeViewModel.class)
    public abstract ViewModel bindScanQRCodeViewModel(ScanQRCodeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserProfileViewModel.class)
    public abstract ViewModel bindUserProfileViewModel(UserProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RewardViewModel.class)
    public abstract ViewModel bindRewardViewModel(RewardViewModel viewModel);

}
