plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.searchai.messaging"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.searchai.messaging"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Hilt Android
    implementation(libs.hilt.android)
    implementation(project(":common"))
    implementation(project(":profile"))
    // Hilt Android Compiler
    kapt(libs.hilt.android.compiler)
    // Logging Interceptor
    implementation(libs.logging.interceptor)
    // Websocket
    implementation(libs.java.websocket)
    // Epoxy
    implementation(libs.epoxy)
    kapt(libs.epoxyProcessor)
    // Emoji
    implementation(libs.emoji.ios)
    // Auth
    implementation(project(":auth"))
    // Viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // Activity
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    // Fragment
    implementation(libs.androidx.fragment.ktx)
    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}