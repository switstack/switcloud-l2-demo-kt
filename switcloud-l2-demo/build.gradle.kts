plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")

    /* Detekt */
    id("io.gitlab.arturbosch.detekt")
}

android {
    namespace = "io.switstack.switcloud.switcloud_l2_demo"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.switstack.switcloud.switcloud_l2_demo"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {

        }
    }

    buildFeatures {
        compose = true
    }

    androidResources {
        noCompress.add("mbn")
    }

    flavorDimensions += "l2"

    productFlavors {

        create("mokastd") {
            dimension = "l2"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.fragment:fragment-ktx:1.5.6")
    implementation("androidx.activity:activity-compose:1.8.0")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2025.11.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.9.6")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

    /* Local AAR */
    implementation(files("libs/switcloud-l2-mokastd-release.aar"))
}

/* Detekt --------------------------------------------------------------------------------------- */

detekt {
    autoCorrect = true
    toolVersion = "1.23.8"
    source.setFrom("src/main/kotlin/")
    config.setFrom("../conf/detekt/detekt.yml")
    buildUponDefaultConfig = true
    basePath = projectDir.absolutePath
    debug = false
}

dependencies {
    detekt("io.gitlab.arturbosch.detekt:detekt-formatting:${detekt.toolVersion}")
    detekt("io.gitlab.arturbosch.detekt:detekt-cli:${detekt.toolVersion}")
}
