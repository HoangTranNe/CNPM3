    // Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id ("com.onesignal.androidsdk.onesignal-gradle-plugin") version "0.14.0" apply false
}

buildscript{
    repositories{
        google()
        gradlePluginPortal()
        maven{url = uri("https://plugins.gradle.org/m2/")}

    }
    dependencies{
        classpath("com.google.gms:google-services:4.4.2")
        classpath ("gradle.plugin.com.onesignal:onesignal-gradle-plugin:[0.12.10, 0.99.99]")

    }
}

// You can add additional configuration here if needed
