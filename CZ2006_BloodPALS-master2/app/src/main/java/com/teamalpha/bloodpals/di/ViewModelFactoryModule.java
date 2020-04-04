package com.teamalpha.bloodpals.di;

import androidx.lifecycle.ViewModelProvider;

import com.teamalpha.bloodpals.presentation.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelFactory);

}
