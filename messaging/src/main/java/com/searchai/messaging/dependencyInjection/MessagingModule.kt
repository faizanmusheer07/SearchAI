package com.searchai.messaging.dependencyInjection

import com.searchai.messaging.peertopeerChat.data.repository.PeerToPeerChatRepositoryImpl
import com.searchai.messaging.peertopeerChat.domain.repository.PeerToPeerChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object MessagingModule {

    @Provides
    @Singleton
    fun providesPeerToPeerChatRepository(okHttpClient: OkHttpClient): PeerToPeerChatRepository = PeerToPeerChatRepositoryImpl(okHttpClient)
}