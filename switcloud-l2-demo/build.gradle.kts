import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
}

val appVersionName = "1.1.1"

fun generateVersionCode(versionName: String): Int {
    val (major, minor, patch) = versionName.split('.').map { it.toInt() }
    return major * 1_000_000 + minor * 1_000 + patch
}

android {
    namespace = "io.switstack.switcloud.switcloud_l2_demo"
    compileSdk = 37

    defaultConfig {
        applicationId = "io.switstack.switcloud.switcloud_l2_demo"
        minSdk = 28
        targetSdk = 36
        versionName = appVersionName
        versionCode = generateVersionCode(appVersionName)

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
            signingConfig = signingConfigs.getByName("debug")
        }

        debug {

        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }

    androidResources {
        noCompress.add("mbn")
    }

    flavorDimensions += "mode"
    flavorDimensions += "target"
    flavorDimensions += "nfcLogo"

    productFlavors {
        create("qualcomm") {
            dimension = "target"
            applicationIdSuffix = ".qcom"
            versionNameSuffix = "-qcom"
            resValue("string", "app_name", "Switcloud L2 Demo Qualcomm")
            missingDimensionStrategy("l2", "mokastd")
        }
        create("sunmi") {
            dimension = "target"
            applicationIdSuffix = ".sunmi"
            versionNameSuffix = "-sunmi"
            resValue("string", "app_name", "Switcloud L2 Demo Sunmi")
            missingDimensionStrategy("l2", "mokastd")
        }
        create("flytech") {
            dimension = "target"
            applicationIdSuffix = ".flytech"
            versionNameSuffix = "-flytech"
            resValue("string", "app_name", "Switcloud L2 Demo Flytech")
            missingDimensionStrategy("l2", "mokastd")
        }
        create("newland") {
            dimension = "target"
            applicationIdSuffix = ".newland"
            versionNameSuffix = "-newland"
            resValue("string", "app_name", "Switcloud L2 Demo Newland")
            missingDimensionStrategy("l2", "mokastd")
        }
        create("authsignal") {
            dimension = "target"
            applicationIdSuffix = ".authsignal"
            versionNameSuffix = "-authsignal"
            resValue("string", "app_name", "Switcloud L2 Demo Authsignal")
            missingDimensionStrategy("l2", "mokastd")
        }
        create("generic") {
            dimension = "target"
            resValue("string", "app_name", "Switcloud L2 Demo")
            missingDimensionStrategy("l2", "mokastd")
        }
        create("standalone") {
            dimension = "mode"
        }
        create("connected") {
            dimension = "mode"
        }
        create("showNfc") {
            dimension = "nfcLogo"
        }
        create("hideNfc") {
            dimension = "nfcLogo"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

androidComponents {
    beforeVariants { variantBuilder ->
        val mode = variantBuilder.productFlavors.find { it.first == "mode" }?.second
        val target = variantBuilder.productFlavors.find { it.first == "target" }?.second
        val nfcLogo = variantBuilder.productFlavors.find { it.first == "nfcLogo" }?.second

        val isConnectedFlytech = (mode == "connected" && target == "flytech")
        val isConnectedSunmi = (mode == "connected" && target == "sunmi")
        val isConnectedNewland = (mode == "connected" && target == "newland")
        val isConnectedAuthsignal = (mode == "connected" && target == "authsignal")
        val isConnectedGeneric = (mode == "connected" && target == "generic")

        val isHideNfcAndNotGeneric = (nfcLogo == "hideNfc" && target != "generic" )

        if (isConnectedFlytech || isConnectedSunmi || isConnectedNewland || isConnectedAuthsignal || isConnectedGeneric || isHideNfcAndNotGeneric) {
            variantBuilder.enable = false
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

base {
    archivesName.set("${rootProject.name}-$appVersionName")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.core)
    implementation(libs.androidx.activity)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // TLV parser / builder
    implementation(libs.tlv)

    /* Serialization */
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    /* Switstack's deps */
    implementation(libs.switcloud.l2)
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
    detektPlugins(libs.bundles.detekt)
}
