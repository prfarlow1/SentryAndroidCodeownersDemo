plugins {
    `kotlin-dsl`
}

group = "com.peterfarlow.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("com.android.tools.build:gradle:8.1.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.core)
    implementation(libs.ktor.kotlinx.serialization)
    implementation(libs.okhttp.logging)
}

gradlePlugin {
    plugins {
        register("traceable") {
            id = "com.peterfarlow.traceable"
            implementationClass = "com.peterfarlow.traceable.TraceableLibraryConventionPlugin"
        }
        register("traceable-app") {
            id = "com.peterfarlow.traceable.app"
            implementationClass = "com.peterfarlow.traceable.TraceableApplicationConventionPlugin"
        }
    }
}
