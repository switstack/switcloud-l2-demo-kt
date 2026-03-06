import io.gitlab.arturbosch.detekt.Detekt

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
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
        buildConfig = true
    }

    androidResources {
        noCompress.add("mbn")
    }

    flavorDimensions += "mode"
    flavorDimensions += "target"
    flavorDimensions += "l2"

    productFlavors {
        create("qualcomm") {
            dimension = "target"
            applicationIdSuffix = ".qcom"
            versionNameSuffix = "-qcom"
            resValue("string", "app_name", "Switcloud L2 Demo Qualcomm")
        }
        create("sunmi") {
            dimension = "target"
            applicationIdSuffix = ".sunmi"
            versionNameSuffix = "-sunmi"
            resValue("string", "app_name", "Switcloud L2 Demo Sunmi")
        }
        create("flytech") {
            dimension = "target"
            applicationIdSuffix = ".flytech"
            versionNameSuffix = "-flytech"
            resValue("string", "app_name", "Switcloud L2 Demo Flytech")
        }
        create("newland") {
            dimension = "target"
            applicationIdSuffix = ".newland"
            versionNameSuffix = "-newland"
            resValue("string", "app_name", "Switcloud L2 Demo Newland")
        }
        create("authsignal") {
            dimension = "target"
            applicationIdSuffix = ".authsignal"
            versionNameSuffix = "-authsignal"
            resValue("string", "app_name", "Switcloud L2 Demo Authsignal")
        }
        create("standalone") {
            dimension = "mode"
        }
        create("connected") {
            dimension = "mode"
        }
        create("mokastd") {
            dimension = "l2"
        }
    }

    androidComponents {
        beforeVariants { variantBuilder ->
            val mode = variantBuilder.productFlavors.find { it.first == "mode" }?.second
            val target = variantBuilder.productFlavors.find { it.first == "target" }?.second

            val isConnectedFlytech = (mode == "connected" && target == "flytech")
            val isConnectedSunmi = (mode == "connected" && target == "sunmi")
            val isConnectedNewland = (mode == "connected" && target == "newland")
            val isConnectedAuthsignal = (mode == "connected" && target == "authsignal")

            if (isConnectedFlytech || isConnectedSunmi || isConnectedNewland || isConnectedAuthsignal) {
                variantBuilder.enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
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

    /* TLV parser / builder */
    implementation("com.payneteasy:ber-tlv:1.0-11")

    /* Serialization */
    implementation("com.squareup.moshi:moshi:1.15.2")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

    /* Switstack's deps */
    implementation("io.switstack.switcloud:switcloud-l2-kt:1.2.0")
    implementation("io.switstack.switcloud:switcloud-api-kt:2.28.0")
}

/* Detekt --------------------------------------------------------------------------------------- */

detekt {
    autoCorrect = true
    toolVersion = "1.23.8"
    source.setFrom("src/main/java/")
    config.setFrom("../conf/detekt/detekt.yml")
    buildUponDefaultConfig = true
    basePath = projectDir.absolutePath
    debug = false
}

tasks.withType<Detekt>().configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true)
        xml.outputLocation.set(file("build/reports/detekt.xml"))
        html.outputLocation.set(file("build/reports/detekt.html"))
    }
}

dependencies {
    detekt("io.gitlab.arturbosch.detekt:detekt-formatting:${detekt.toolVersion}")
    detekt("io.gitlab.arturbosch.detekt:detekt-cli:${detekt.toolVersion}")
}
