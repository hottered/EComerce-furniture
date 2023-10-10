plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt") version "1.9.10"
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.ecomerce"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.ecomerce"
        minSdk = 21
        targetSdk = 33
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        
    }
    tasks.withType<org.jetbrains.kotlin.gradle.internal.KaptWithoutKotlincTask>()
        .configureEach {
            kaptProcessJvmArgs.add("-Xmx512m")
        }
    kapt {
        correctErrorTypes = true
        useBuildCache = false
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.firebase:firebase-storage-ktx:20.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //Firebase
    implementation("com.google.firebase:firebase-auth-ktx:22.1.2")
    implementation("com.google.firebase:firebase-firestore-ktx:24.8.1")
//    implementation ("com.google.firebase:firebase-auth:21.0.6")


    //Navigation component
    val nav_version = "2.5.2"
    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")
    
    //loading button
//    implementation ("br.com.simplepass:loading-button-android:2.2.0")
    implementation("com.github.leandroborgesferreira:loading-button-android:2.3.0")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.13.0")

    //circular image
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //viewpager2 indicatior
//    implementation ("com.github.vejei.viewpagerindicator:viewpagerindicator:1.0.0-alpha.1")
    implementation ("com.github.zhpanvip:viewpagerindicator:1.2.3")

    //stepView
    implementation ("com.github.shuhart:stepview:1.5.1")

    //Android Ktx
    implementation ("androidx.navigation:navigation-fragment-ktx:2.4.2")

    //Dagger hilt
    implementation ("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    
    //Coroutines with firebase
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.1")
    

}