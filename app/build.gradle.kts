plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.calorieapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.calorieapp"
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (platform(libs.firebase.bom))
    implementation (libs.google.firebase.database)
    implementation (libs.google.firebase.storage) // For image storage
    implementation (libs.glide)  // For loading images
    implementation (libs.com.google.firebase.firebase.database)
    implementation (libs.com.google.firebase.firebase.storage)
    implementation (libs.firebase.database.v2005)
    implementation (libs.firebase.database.v2010)
    implementation (libs.firebase.storage.v2000)
    implementation (libs.firebase.firestore.v2410)

    implementation (libs.androidx.core.ktx.v1120)
    implementation (libs.androidx.appcompat.v162)
    implementation (libs.firebase.firestore.v2471) // Update to the latest version
    implementation (libs.firebase.analytics) // Optional
    implementation (libs.firebase.firestore.v2400)

    implementation (libs.glide.v4120)
    annotationProcessor (libs.compiler)








}