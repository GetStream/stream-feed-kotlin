package io.getstream.feed

object Versions {
    internal const val ANDROID_GRADLE_PLUGIN = "7.0.3"
    internal const val GRADLE_VERSIONS_PLUGIN = "0.39.0"
    internal const val KOTLIN = "1.5.31"
    internal const val KTLINT_PLUGIN = "10.2.0"
}

object Dependencies {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}"
    const val gradleVersionsPlugin = "com.github.ben-manes:gradle-versions-plugin:${Versions.GRADLE_VERSIONS_PLUGIN}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val ktlintPlugin = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.KTLINT_PLUGIN}"

    @JvmStatic
    fun isNonStable(version: String): Boolean = isStable(version).not()

    @JvmStatic
    fun isStable(version: String): Boolean = ("^[0-9.]+$").toRegex().matches(version)
}
