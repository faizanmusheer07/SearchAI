package com.searchai.api.extensions

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ProvideRetrofitServices @Inject constructor(
    @Named("auth")private val retrofit: Retrofit)
{

    fun <T>createService(serviceClass:Class<T>):T{
        return retrofit.create(serviceClass)
    }
}