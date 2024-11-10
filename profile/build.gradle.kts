plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.searchai.profile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.searchai.profile"
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
        dataBinding = true
        // Add this to dataBinding
        buildConfig = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {


    implementation("com.squareup.retrofit2:retrofit:")
    // Hilt Android
    implementation(libs.hilt.android)
    implementation(libs.androidx.ui.android)
    implementation(project(":common"))
    // Hilt Android Compiler
    kapt(libs.hilt.android.compiler)
    // Refresh Dependency
    implementation(libs.jamshid.m.igrefreshlayout)
    // Common
//    implementation(project(":common"))
    // Api
    implementation(project(":api"))
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
    // Firebase
    implementation(platform(libs.firebase.bom))
    // Firebase Auth
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
    //epoxy
    implementation(libs.epoxy)
    ksp(libs.epoxyProcessor)

    //shimmer
    implementation(libs.shimmer)

    // Retrofit and Okhttp
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.squareup.converter.moshi)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    //picasso
    implementation(libs.picasso)
    //circularImageView
    implementation(libs.circleimageview)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.databinding:databinding-runtime:7.x.x")
//    implementation ("de.hdodenhof:circleimageview:3.1.0")

// Add this for video compressor
    implementation("com.example:video-compressor:1.0.0")
    implementation ("com.iceteck.silicompressorr:silicompressor:2.2.4")
    implementation ("com.googlecode.mp4parser:isoparser:1.1.22")
}