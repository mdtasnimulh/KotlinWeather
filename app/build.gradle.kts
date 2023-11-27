plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
    //id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.tasnim.chowdhury.kotlinweather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tasnim.chowdhury.kotlinweather"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures{
        dataBinding = true
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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //-----jetbrains kotlin extension
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.8.22")

    //-----network communicator and json parsing
    implementation ("com.google.code.gson:gson:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.8.1")
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("io.grpc:grpc-okhttp:1.52.1")
    implementation ("com.localebro:okhttpprofiler:1.0.8")

    //----navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.5")

    //-----google maps
    implementation ("com.google.android.gms:play-services-maps:18.2.0")

    //moshi
    implementation ("com.squareup.moshi:moshi:1.9.2")
    implementation ("com.squareup.moshi:moshi-kotlin:1.9.2")

    //view-model and livedata
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // Glide
    /*implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.caverock:androidsvg-aar:1.4")
    ksp ("com.github.bumptech.glide:ksp:4.12.0")*/
}