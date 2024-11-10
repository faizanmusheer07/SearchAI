plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.searchai.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Hilt Android Compiler
    kapt(libs.hilt.android.compiler)

    // SplashScreen
    implementation(libs.androidx.core.splashscreen)

    //gson
    implementation(libs.gson)

    //okHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //retrofit
    implementation(libs.retrofit)

    // Datastore Preferences
    implementation(libs.androidx.datastore.preferences)

    //project dependencies
//    implementation(project(":api"))
//    implementation(project(":auth"))
//    implementation(project(":explore"))

    // Epoxy
    implementation(libs.epoxy)
    ksp(libs.epoxyProcessor)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.viewmodel.ktx.v287)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.video.compressor)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.databinding:databinding-runtime:7.x.x")
    implementation("com.example:video-compressor:1.0.0")
// Add this for video compressor
    implementation ("com.iceteck.silicompressorr:silicompressor:2.2.4")
    implementation ("com.googlecode.mp4parser:isoparser:1.1.22")

    implementation("androidx.databinding:databinding-runtime:7.x.x")
}