package com.searchai.common.kotlinExtentions.dependencyInjection

import com.searchai.common.kotlinExtentions.checkNetwork.CheckInternet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonDependencies {

    @Provides
    @Singleton
    fun provideCheckInternetInstance():CheckInternet{
        return CheckInternet()
    }

}