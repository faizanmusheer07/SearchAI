plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    id("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.searchai.myworld"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.searchai.myworld"
        minSdk = 24
        targetSdk = 35
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
    }
}

dependencies {
    // Hilt Android
    implementation(libs.hilt.android)
    // Hilt Android Compiler
    kapt(libs.hilt.android.compiler)
    // onboarding
    implementation(project(":onboarding"))
    // Common
    implementation(project(":common"))
    // Navigation
    implementation(libs.androidx.navigation.fragment)
    // Profile
    implementation(project(":profile"))
    //My Room
    implementation(project(":myroom"))
    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.auth)
    // Auth
    implementation(project(":auth"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

//
//    implementation common_impl
//            implementation navigation
//            implementation ("com.android.support:multidex:2.0.1")
//
//    implementation ("org.webrtc:google-webrtc:1.0.30039")
//    implementation("io.socket:socket.io-client:2.0.1") /*{
//        // excluding org.json which is provided by Android
//        exclude group: 'org.json', module: 'json'
//    }*/
//    //noinspection GradleCompatible
//    implementation ("com.android.support:design:28.0.0")
//    implementation ("com.google.android.flexbox:flexbox:3.0.0")
//
//    implementation ("com.android.support.constraint:constraint-layout:2.0.4")
//    //noinspection GradleCompatible
//    implementation ("com.android.support:design:28.0.0-rc01")
//    implementation ("androidx.constraintlayout:constraintlayout:2.1.3")
//    implementation("androidx.recyclerview:recyclerview:1.2.1")
//    // For control over item selection of both touch and mouse driven selection
//    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
//
//    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
//    implementation epoxy.impl
//            implementation project(path: ':newapi')
//    implementation project(path: ':newapi')
//    implementation 'com.google.firebase:firebase-database-ktx:20.0.3'
//    implementation project(path: ':newapi')
//    implementation project(path: ':model')
//    implementation project(path: ':myspace')
//    kapt epoxy.kap
//
//            implementation hilt_lib.impl
//            kapt hilt_lib.kap
//            implementation 'com.github.prabhat1707:VerticalViewPager:1.0'
//
//    implementation exoplayer
//
//            implementation gson
//            implementation glide
//            implementation webrtc
//            implementation flexbox
//            implementation cardview
//            implementation viewpager2
//            implementation swipe_refresh
//            implementation circle_image_view
//
//            implementation project(":commonui")
//    implementation project(":profile")
////    implementation "androidx.core:core-ktx:1.5.0"
//    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
//
//    // blurkit
//    implementation 'com.github.CameraKit:blurkit-android:v1.1.0'
//
//    // Epoxy
//    def epoxyVersion = "4.6.4"
//    implementation "com.airbnb.android:epoxy:$epoxyVersion"
//    implementation "com.airbnb.android:epoxy-paging:$epoxyVersion"
//    implementation ("com.airbnb.android:epoxy-paging3:$epoxyVersion"
//    implementation ("androidx.paging:paging-rxjava2:2.1.0")
//    implementation ("com.jakewharton.rxrelay2:rxrelay:2.1.0")
////    implementation 'com.airbnb.android:epoxy-paging:3.6.0'
//    implementation ("androidx.lifecycle:lifecycle-extensions:2.0.0")
//
//
//
////    implementation "com.android.support:cardview-v7:27.1.1"
//
//    // Paging
//    def paging_version = "3.0.0"
//    implementation ("androidx.paging:paging-runtime-ktx:$paging_version")
//
//
//
//
//}
//repositories {
//    mavenCentral()
//    maven {
//        val uri = url "https://jitpack.io" }
}