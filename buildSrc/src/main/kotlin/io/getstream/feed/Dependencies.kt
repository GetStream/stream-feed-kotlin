package io.getstream.feed

object Versions {
    internal const val ANDROID_GRADLE_PLUGIN = "7.0.3"
    internal const val ANDROID_JUNIT5_GRADLE_PLUGIN = "1.8.0.0"
    internal const val COROUTINES = "1.5.2"
    internal const val DOKKA = "1.5.31"
    internal const val GRADLE_NEXUS_PUBLISH_PLUGIN = "1.1.0"
    internal const val GRADLE_VERSIONS_PLUGIN = "0.39.0"
    internal const val JUNIT5 = "5.8.1"
    internal const val KLUENT = "1.68"
    internal const val KOTLIN = "1.5.31"
    internal const val KTLINT_PLUGIN = "10.2.0"
    internal const val MOCKITO = "4.0.0"
    internal const val MOCKITO_KOTLIN = "2.2.0"
    internal const val MOSHI = "1.12.0"
    internal const val OK2CURL = "0.7.0"
    internal const val OKHTTP = "4.9.2"
    internal const val RETROFIT = "2.9.0"
}

object Dependencies {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}"
    const val androidJunit5GradlePlugin =
        "de.mannodermaus.gradle.plugins:android-junit5:${Versions.ANDROID_JUNIT5_GRADLE_PLUGIN}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES}"
    const val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.DOKKA}"
    const val gradleNexusPublishPlugin = "io.github.gradle-nexus:publish-plugin:${Versions.GRADLE_NEXUS_PUBLISH_PLUGIN}"
    const val gradleVersionsPlugin = "com.github.ben-manes:gradle-versions-plugin:${Versions.GRADLE_VERSIONS_PLUGIN}"
    const val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT5}"
    const val junitJupiterParams = "org.junit.jupiter:junit-jupiter-params:${Versions.JUNIT5}"
    const val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT5}"
    const val kluent = "org.amshove.kluent:kluent:${Versions.KLUENT}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val ktlintPlugin = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.KTLINT_PLUGIN}"
    const val mockito = "org.mockito:mockito-core:${Versions.MOCKITO}"
    const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.MOCKITO_KOTLIN}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.MOSHI}"
    const val moshiAdapters = "com.squareup.moshi:moshi-adapters:${Versions.MOSHI}"
    const val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.MOSHI}"
    const val ok2curl = "com.github.mrmike:ok2curl:${Versions.OK2CURL}"
    const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.RETROFIT}"

    @JvmStatic
    fun isNonStable(version: String): Boolean = isStable(version).not()

    @JvmStatic
    fun isStable(version: String): Boolean = ("^[0-9.]+$").toRegex().matches(version)
}
