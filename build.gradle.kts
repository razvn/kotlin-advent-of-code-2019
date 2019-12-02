plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.61"

    // Apply the application plugin to add support for building a CLI application.
    application
}

repositories {
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    mainClassName = "AppKt"
}
