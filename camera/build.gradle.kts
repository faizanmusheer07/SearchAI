plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.searchai.camera"
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
    }
}

dependencies {
    // Hilt Android
    implementation(libs.hilt.android)
    implementation(libs.ui.android)

    // Hilt Android Compiler
    kapt(libs.hilt.android.compiler)

    //okHttp
    implementation(libs.okhttp)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Epoxy
    implementation(libs.epoxy)
    implementation(libs.epoxy.databinding)
    kapt(libs.epoxyProcessor)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Circle Image View
    implementation(libs.circleimageview)

    // Project dependencies
    implementation(project(":common"))
    implementation(project(":api"))
    implementation(project(":myroom"))

    //implementation(project(":myspace"))
    //implementation(project(":newrepository"))

    // External libraries
    implementation(libs.kurento.client) // Kurento Client

    //Camera
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.camera.camera2)


    implementation(libs.exoplayer) // ExoPlayer

    api(libs.video.compressor) // Video Compressor

    implementation(libs.glide) // Glide
    implementation(libs.cardview) // CardView

    //transcoder
    implementation(libs.transcoder)

    implementation("com.jakewharton:butterknife:${libs.versions.butterKnife}") // Use libs reference
    kapt("com.jakewharton:butterknife-compiler:${libs.versions.butterKnife}") // Use libs reference
}