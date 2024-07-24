import org.gradle.internal.impldep.bsh.commands.dir

plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.dating_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dating_app"
        minSdk = 25
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

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    
    //Circle ImageView
    //implementation ("de.hdodenhof:circleimageview:2.2.0")
    //implementation ("com.github.dimorinny:show-case-card-view:0.0.1")
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")

    implementation ("com.github.bumptech.glide:glide:3.7.0")
    implementation ("com.lorentzos.swipecards:library:1.0.9")
    implementation ("com.google.android.material:material:1.1.0-alpha03")
    implementation ("de.hdodenhof:circleimageview:3.1.0")


    implementation ("com.github.dimorinny:show-case-card-view:0.0.4")

    //implementation ("com.github.BakerJQ:Android-InfiniteCards:1.0.5")
    //implementation 'com.google.firebase:firebase-ads:19.7.0'

    //youtube dependency

    implementation ("com.onesignal:OneSignal:3.10.1")


    implementation ("com.google.firebase:firebase-auth:17.0.0")
    implementation ("com.google.firebase:firebase-storage:17.0.0")
    implementation ("com.google.firebase:firebase-messaging:19.0.0")

    implementation ("com.github.bumptech.glide:glide:3.7.0")
    implementation ("com.lorentzos.swipecards:library:1.0.9")
    implementation ("com.google.firebase:firebase-core:17.0.0")
    implementation ("com.google.android.material:material:1.1.0-alpha03")
    implementation ("com.google.firebase:firebase-database:18.0.0")
    //navigation
    implementation ("com.github.ittianyu:BottomNavigationViewEx:1.2.4")

    //Circle ImageView
    implementation ("de.hdodenhof:circleimageview:2.2.0")
    implementation ("com.github.dimorinny:show-case-card-view:0.0.1")

    implementation ("com.github.BakerJQ:Android-InfiniteCards:1.0.5")
    implementation ("com.google.firebase:firebase-ads:19.7.0")

//    implementation 'com.google.android.gms:play-services-ads:19.7.0'
//
//    implementation 'com.google.android.gms:play-services-basement:17.6.0'


}