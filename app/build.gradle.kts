plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.google.services)
    id("com.google.firebase.crashlytics")
    kotlin("kapt")
}

android {
    namespace = "com.pettranslator.cattranslator.catsounds"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pettranslator.cattranslator.catsounds"
        minSdk = 24
        targetSdk = 35
        versionCode = 11
        versionName = "0.0.11"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("keystore/hutoca.keystore")
            storePassword = "ntstore"
            keyAlias = "hutoca"
            keyPassword = "ntstore"
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            buildConfigField(
                "String",
                "BANNER_AD_UNIT_ID",
                "\"ca-app-pub-7340251527995818/9792653557\""
            )
            buildConfigField(
                "String",
                "INTERSTITIAL_AD_UNIT_ID",
                "\"ca-app-pub-7340251527995818/6284894630\""
            )
            buildConfigField(
                "String",
                "APP_OPEN_AD_UNIT_ID",
                "\"ca-app-pub-7340251527995818/7166490211\""
            )
            buildConfigField(
                "String",
                "NATIVE_AD_UNIT_ID",
                "\"ca-app-pub-7340251527995818/5854468032\""
            )
            buildConfigField(
                "String",
                "REWARD_AD_ID",
                "\"ca-app-pub-7340251527995818/4971812967\""
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
        debug {
            buildConfigField(
                "String",
                "BANNER_AD_UNIT_ID",
                "\"ca-app-pub-3940256099942544/6300978111\""
            )
            buildConfigField(
                "String",
                "INTERSTITIAL_AD_UNIT_ID",
                "\"ca-app-pub-3940256099942544/1033173712\""
            )
            buildConfigField(
                "String",
                "APP_OPEN_AD_UNIT_ID",
                "\"ca-app-pub-3940256099942544/9257395921\""
            )
            buildConfigField(
                "String",
                "NATIVE_AD_UNIT_ID",
                "\"ca-app-pub-3940256099942544/2247696110\""
            )
            buildConfigField(
                "String",
                "REWARD_AD_ID",
                "\"ca-app-pub-3940256099942544/5224354917\""
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    dataBinding {
        enable = true
    }

    bundle {
        language {
            enableSplit = false
        }
    }


    // Tùy chỉnh tên file APK/AAB
    applicationVariants.configureEach {
        outputs.configureEach {
            val appName = "cat-translator" // Tên ứng dụng
            val versionName = versionName // Truy cập versionName từ variant
            val buildType = buildType.name // Truy cập buildType từ variant

            // Đặt tên file APK/AAB
            val newOutputFileName = when {
                this is com.android.build.gradle.internal.api.BaseVariantOutputImpl &&
                        outputFileName.endsWith(".aab") -> "$appName-v$versionName-$buildType.aab"

                else -> "$appName-v$versionName-$buildType.apk"
            }

            if (this is com.android.build.gradle.internal.api.BaseVariantOutputImpl) {
                outputFileName = newOutputFileName
            }
        }
    }
}



dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Glide
    implementation(libs.glide)
    kapt(libs.compiler)
    implementation(libs.converter.gson)

    //AdMod
    implementation(libs.play.services.ads)

    implementation(libs.androidx.lifecycle.process)

    //di
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    //
    implementation(libs.shimmer)

    implementation(libs.viewpagerdotsindicator)

    // WorkManager
    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.firebase.analytics)
    implementation(platform(libs.firebase.bom))

    // Add the dependencies for the Remote Config and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.config)

    implementation("com.google.ads.mediation:inmobi:10.8.3.1")
//    implementation("com.google.ads.mediation:ironsource:8.9.0.0")
    implementation("com.google.ads.mediation:vungle:7.5.0.0")
    implementation("com.google.ads.mediation:mintegral:16.9.71.0")
    implementation("com.google.ads.mediation:pangle:7.2.0.3.0")
//    implementation("com.google.ads.mediation:unity:4.15.0.0")
    implementation("com.google.android.ump:user-messaging-platform:2.1.0")
    implementation("com.github.ertugrulkaragoz:SuperBottomBar:0.4")
    implementation("com.google.firebase:firebase-crashlytics-ndk")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.code.gson:gson:2.11.0");
}