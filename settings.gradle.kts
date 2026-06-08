pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    val storageUrl: String = System.getenv("FLUTTER_STORAGE_BASE_URL") ?: "https://storage.googleapis.com"
    repositories {
        google()
        mavenCentral()
        maven("$storageUrl/download.flutter.io")
    }
}

rootProject.name = "mario"
include(":app")
val filePath = "$settingsDir/flutter_module/.android/include_flutter.groovy"
val flutterInclude = File(filePath)
check(flutterInclude.exists()) {
    "Missing Flutter generated Android wrapper at $filePath. " +
        "Install the Flutter SDK, then run `cd flutter_module && flutter pub get` " +
        "to generate .android before building the host app."
}
apply(from = flutterInclude)
