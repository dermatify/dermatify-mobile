import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.bangkit.android.dermatify"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bangkit.android.dermatify"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        resourceConfigurations += mutableSetOf("en", "in")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        fun getKey(key: String): Any?{
            val keysFile = project.rootProject.file("local.properties")
            val properties = Properties()
            properties.load(FileInputStream(keysFile))
            return properties[key]
        }

        buildConfigField("String", "BASE_URL", "\"${getKey("BASE_URL")}\"")
        buildConfigField("String", "WEB_CLIENT_ID", "\"${getKey("WEB_CLIENT_ID")}\"")
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Credentials & Auth
//    implementation("com.google.android.gms:play-services-auth:20.4.1")
    implementation("androidx.credentials:credentials:1.2.2")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.2")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")


    // Data Store
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Fragment
    implementation("androidx.fragment:fragment-ktx:1.8.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Lottie
    implementation("com.airbnb.android:lottie:6.4.0")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")
}