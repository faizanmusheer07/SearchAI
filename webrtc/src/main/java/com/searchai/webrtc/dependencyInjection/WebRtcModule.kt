package com.searchai.webrtc.dependencyInjection

import android.content.Context
import android.util.Log
import com.searchai.webrtc.conference.client.SignallingClient
import com.searchai.webrtc.constants.WebRtcConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.client.SocketIOException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import java.net.URISyntaxException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


@Module
@InstallIn(SingletonComponent::class)
object WebRtcModule {

    @Provides
    @Singleton
    fun providesSocketIoClient(okHttpClient: OkHttpClient): Socket {
        try {
            val opts = IO.Options().apply {
                reconnection = true
                this.callFactory = okHttpClient
            }
            val socket = IO.socket(WebRtcConstants.CONFERENCE_SOCKET_URL, opts)
            Log.d("Socket", "Socket created: $socket")
            socket.connect()
            Log.d("Socket", "Socket connected: ${socket.connected()}")
            return socket
        } catch (e: URISyntaxException) {
            Log.e("Socket", "URISyntaxException: ${e.localizedMessage}")
            throw e
        } catch (e: SocketIOException) {
            Log.e("Socket", "SocketIOException: ${e.localizedMessage}")
            throw e
        } catch (e: Exception) {
            Log.e("Socket", "Exception: ${e.localizedMessage}")
            throw e
        }
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val sslContext = SSLContext.getInstance("TLS")

        sslContext.init(null, arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun checkServerTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }), java.security.SecureRandom())

        val trustAllCerts: X509TrustManager = object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun checkServerTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }

        val sslSocketFactory = sslContext.socketFactory
        val hostnameVerifier = HostnameVerifier { _, _ -> true }

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.MINUTES)
            .readTimeout(1000, TimeUnit.MINUTES)
            .writeTimeout(1000, TimeUnit.MINUTES)
            .addInterceptor(loggingInterceptor)
            .sslSocketFactory(sslSocketFactory, trustAllCerts)
            .hostnameVerifier(hostnameVerifier)
            .build()
        return okHttpClient
    }

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun providesEglBaseContext(): EglBase.Context = EglBase.create().eglBaseContext

    @Provides
    @Singleton
    fun providesPeerConnectionFactory(
        @ApplicationContext context: Context,
        eglBaseContext: EglBase.Context
    ): PeerConnectionFactory {
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context)
                .setEnableInternalTracer(true)
                .createInitializationOptions()
        )
        val options = PeerConnectionFactory.Options()
        options.disableEncryption = false;
        options.disableNetworkMonitor = false;
        val peerConnectionFactory = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(DefaultVideoEncoderFactory(eglBaseContext, true, true))
            .setVideoDecoderFactory(DefaultVideoDecoderFactory(eglBaseContext))
            .setOptions(options).createPeerConnectionFactory()
        peerConnectionFactory.createVideoSource(false)
        peerConnectionFactory.createAudioSource(MediaConstraints())

        return peerConnectionFactory
    }

    @Provides
    @Singleton
    fun providesRtcConfig(): PeerConnection.RTCConfiguration {
        val iceServers = listOf(
            PeerConnection.IceServer.builder(WebRtcConstants.CONFERENCE_STUN_SERVER)
                .createIceServer()
        )
        return PeerConnection.RTCConfiguration(iceServers).apply {
            iceTransportsType = PeerConnection.IceTransportsType.ALL
            sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
        }
    }

    @Provides
    @Singleton
    fun providesSignallingClient(
        socket: Socket,
        rtcConfig: PeerConnection.RTCConfiguration,
        peerConnectionFactory: PeerConnectionFactory
    ): SignallingClient = SignallingClient(
        socket = socket,
        rtcConfig = rtcConfig,
        peerConnectionFactory = peerConnectionFactory
    )
}