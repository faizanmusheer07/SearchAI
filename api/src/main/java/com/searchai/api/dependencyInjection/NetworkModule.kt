package com.searchai.api.dependencyInjection

import com.searchai.api.constants.auth.AuthConstants
import com.searchai.api.constants.followfollowing.FollowFollowingConstants
import com.searchai.api.constants.interests.InterestsConstants
import com.searchai.api.constants.myroom.MyRoomConstants
import com.searchai.api.services.auth.AuthService
import com.searchai.api.services.followfollowing.FollowFollowingService
import com.searchai.api.services.interests.InterestSubmissionService
import com.searchai.api.services.interests.InterestsService
import com.searchai.api.services.myroom.MyRoomService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val hostnameVerifier = HostnameVerifier { hostname, session -> true }
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
        val trustManager = trustAllCerts[0] as X509TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, null)

        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .hostnameVerifier(hostnameVerifier)
            .sslSocketFactory(sslSocketFactory, trustManager)
            .callTimeout(120, TimeUnit.SECONDS)
            .build()
        return okHttpClient
    }

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


    @Provides
    @Singleton
    @Named("auth")
    fun providesRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(AuthConstants.BASE_ENDPOINT)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun providesAuthService(@Named("auth") retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    @Named("interests")
    fun providesRetrofitInterests(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(InterestsConstants.BASE_ENDPOINT)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    @Provides
    @Singleton
    fun providesInterestsService(@Named("interests") retrofit: Retrofit): InterestsService = retrofit.create(InterestsService::class.java)


    /** for submit the selected interest chip*/
    @Provides
    @Singleton
    @Named("interests_submission")
    fun providesRetrofitInterestsSubmission(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(InterestsConstants.BASE_ENDPOINT2)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideInterestsService(
        @Named("interests_submission") retrofit: Retrofit): InterestSubmissionService = retrofit.create(InterestSubmissionService::class.java)

    /** for my_room module*/
    @Provides
    @Singleton
    @Named("my_room")
    fun providesRetrofitMyRoom(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(MyRoomConstants.BASE_ENDPOINT)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideMyRoomService(
        @Named("my_room") retrofit: Retrofit): MyRoomService = retrofit.create(MyRoomService::class.java)

    /** follow following*/
    @Provides
    @Singleton
    @Named("follow_following")
    fun providesRetrofitFollowFollowing(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(FollowFollowingConstants.BASE_ENDPOINT)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideFollowFollowingService(
        @Named("follow_following") retrofit: Retrofit ) : FollowFollowingService = retrofit.create(FollowFollowingService::class.java)
}