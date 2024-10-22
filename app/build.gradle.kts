plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.hfad.filmdex"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hfad.filmdex"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation ("com.squareup.picasso:picasso:2.71828")

    implementation ("com.google.android.material:material:1.5.0")// проверьте последнюю версию
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.nostra13.universalimageloader:universal-image-loader:1.9.5")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.cardview:cardview:1.0.0")

}