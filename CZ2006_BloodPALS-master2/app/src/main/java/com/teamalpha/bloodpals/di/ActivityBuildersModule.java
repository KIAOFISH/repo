package com.teamalpha.bloodpals.di;

import com.teamalpha.bloodpals.di.main.MainScope;
import com.teamalpha.bloodpals.presentation.MainActivity;
import com.teamalpha.bloodpals.di.main.MainFragmentBuildersModule;
import com.teamalpha.bloodpals.di.main.MainModule;
import com.teamalpha.bloodpals.di.main.MainViewModelsModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @MainScope
    @ContributesAndroidInjector(
            modules = {
                    MainFragmentBuildersModule.class,
                    MainViewModelsModule.class,
                    MainModule.class
            })
    abstract MainActivity contributeMainActivity();

}
