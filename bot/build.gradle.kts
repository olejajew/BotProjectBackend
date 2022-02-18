plugins {
    kotlin("jvm")
    java
}

group = "com.doblack"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    api("org.telegram:telegrambots:5.4.0.1")
    api("com.google.code.gson:gson:2.9.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    api("joda-time:joda-time:2.10.13")
    implementation(project(":core"))
    //todo Вот это не оч
    implementation("com.google.firebase:firebase-admin:8.1.0")


}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}