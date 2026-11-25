import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm()
    android {
       namespace = "com.sae.multiplatform.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
       withDeviceTestBuilder {
           sourceSetTreeName = "test"
       }.configure {
           instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }
    }
    sourceSets {
        androidMain.dependencies {
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.compose.uiTooling)

    // Motor HTTP para Android
    implementation(libs.ktor.client.okhttp)
}
     jvmMain.dependencies {
    // Motor HTTP para Desktop
    implementation(libs.ktor.client.cio)
}
commonMain.dependencies {
    implementation(libs.compose.runtime)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.components.resources)
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.androidx.lifecycle.viewmodelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    // Supabase compartido entre Android y Desktop
    implementation(libs.supabase.auth)
    implementation(libs.supabase.postgrest)
    // Cliente HTTP para Open Library
    implementation(libs.ktor.client.core)
    // Carga de portadas de libros
    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor3)
    // Persistencia local de favoritos y bienestar
    implementation(libs.multiplatform.settings)
}
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        getByName("androidDeviceTest").dependencies {
            implementation(libs.androidx.testRunner)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

// El logo y los demás recursos de Compose se comparten entre Android y Desktop.
// Debe coincidir con los imports de WelcomeScreen y AboutScreen.
compose.resources {
    packageOfResClass = "com.sae.multiplatform.resources"
    generateResClass = always
}
