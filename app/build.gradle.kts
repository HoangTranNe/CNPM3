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
        //noinspection OldTargetApi
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

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    implementation (libs.glide)
    implementation (libs.library)
    implementation (libs.material)
    implementation ("de.hdodenhof:circleimageview:3.1.0")


    implementation ("com.github.dimorinny:show-case-card-view:0.0.4")

    //youtube dependency

    implementation (libs.onesignal)


    implementation (libs.firebase.auth.v1700)
    implementation (libs.firebase.storage)
    implementation (libs.firebase.messaging)

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
    implementation ("com.github.ittianyu:BottomNavigationViewEx:2.0.4")
    implementation (libs.android.infinitecards)

//
    implementation ("com.google.android.gms:play-services-basement:17.6.0")

}
apply(plugin = "com.google.gms.google-services")

apply(plugin = "com.onesignal.androidsdk.onesignal-gradle-plugin")
