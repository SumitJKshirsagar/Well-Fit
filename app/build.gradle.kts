plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.well_fit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.well_fit"
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
}

buildscript {
    repositories {
        mavenCentral()
    }


}
dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.google.android.gms:play-services-auth:20.4.0")
    implementation("com.google.firebase:firebase-firestore:24.4.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("io.github.ShawnLin013:number-picker:2.4.13")
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("com.google.android.material:material:1.4.0")
    implementation("com.intuit.ssp:ssp-android:1.0.6")
    implementation ("com.github.dangiashish:Auto-Image-Slider:1.0.6")
    implementation ("com.squareup.picasso:picasso:2.71828")





}