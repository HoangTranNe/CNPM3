    // Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

}

buildscript{
    repositories{
        google()
    }
    dependencies{
        classpath("com.google.gms:google-services:4.4.2")
    }
}

// You can add additional configuration here if needed
