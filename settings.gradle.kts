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
val flutterModuleDir = File(settingsDir, "flutter_module")
val groovyFile = File(flutterModuleDir, ".android/include_flutter.groovy")

// Auto-detect if Flutter SDK is installed and available in PATH
val isFlutterInstalled = try {
    val isWindows = System.getProperty("os.name").lowercase().contains("windows")
    val flutterExecutable = if (isWindows) "flutter.bat" else "flutter"
    val process = ProcessBuilder(flutterExecutable, "--version").start()
    process.waitFor() == 0
} catch (e: Exception) {
    false
}

val includeFlutter = isFlutterInstalled

gradle.extensions.extraProperties.set("mario.includeFlutter", includeFlutter)

if (includeFlutter) {
    if (!groovyFile.exists()) {
        println("Flutter build files not found. Running 'flutter pub get' in flutter_module...")
        val isWindows = System.getProperty("os.name").lowercase().contains("windows")
        val flutterExecutable = if (isWindows) "flutter.bat" else "flutter"
        try {
            val process = ProcessBuilder(flutterExecutable, "pub", "get")
                .directory(flutterModuleDir)
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()
            val exitCode = process.waitFor()
            if (exitCode != 0) {
                System.err.println("Warning: 'flutter pub get' failed with exit code $exitCode")
            }
        } catch (e: Exception) {
            System.err.println("Warning: Could not run 'flutter pub get' automatically: ${e.message}")
        }
    }

    if (groovyFile.exists()) {
        apply(from = groovyFile)
    } else {
        System.err.println("Error: Flutter integration files are missing and could not be generated.")
    }
} else {
    println("Flutter integration is disabled (Flutter SDK not found).")
}

