pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven(url = "https://jitpack.io")
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://android-sdk.is.com/") } // ironSource
        maven { url = uri("https://repo.ironsrc.com/artifactory/mobile-sdk/") } // ironSource backup
        maven { url = uri("https://sdk.unityads.unity3d.com/release") } // Unity Ads
        maven { url = uri("https://artifact.bytedance.com/repository/pangle") } // Pangle
        maven { url = uri("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea") }
        maven { url = uri("https://jitpack.io")}
    }
}

rootProject.name = "CatTranslator"
include(":app")
 