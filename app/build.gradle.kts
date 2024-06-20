import java.time.Clock
import java.time.Instant

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("io.sentry.android.gradle").version("4.6.0")
    kotlin("plugin.serialization") version "2.0.0-RC3"
    id("com.peterfarlow.traceable.app")
}

android {
    namespace = "com.peterfarlow.sentryandroidcodeownersdemo"
    compileSdk = 34

    defaultConfig {
        val millis = Instant.now(Clock.systemUTC()).epochSecond.toInt()
        applicationId = "com.peterfarlow.sentryandroidcodeownersdemo"
        minSdk = 33
        targetSdk = 34
        versionCode = millis
        versionName = "1.0.$millis"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildFeatures.buildConfig = true
    }

    signingConfigs {
        create("release") {
            keyAlias = "alias"
            keyPassword = "password"
            storePassword = "password"
            storeFile = file("../commonkeystore.jks")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

sentry {
    debug.set(true)
    includeSourceContext.set(true)
    org.set("codeowners-demo")
    projectName.set("android")
    additionalSourceDirsForSourceContext.set(setOf("detail/src/main/java", "core/src/main/java"))
    authToken.set(System.getenv("SENTRY_AUTH_TOKEN3"))
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation(project(":detail"))
    implementation(project(":core"))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
