plugins {
    kotlin("jvm") version "2.1.10"
    id("com.github.johnrengelman.shadow") version "8.+"
}

group = "org.cubewhy.celestial"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.fabricmc:mapping-io:0.7.1")
    compileOnly("org.ow2.asm:asm:9.7.1")
    compileOnly("org.ow2.asm:asm-tree:9.7.1")
    compileOnly("org.ow2.asm:asm-commons:9.7.1")
}

tasks.jar {
    dependsOn("shadowJar")

    archiveClassifier.set("")
    archiveVersion.set("")
    manifest {
        attributes(
            "Premain-Class" to "org.cubewhy.celestial.tweaks.Agent",
            "Can-Redefine-Classes" to "true",
            "Can-Retransform-Classes" to "true"
        )
    }
}

tasks.shadowJar {
    archiveClassifier.set("fatjar")
    archiveVersion.set("")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    exclude("native-binaries/**")

    exclude("LICENSE.txt")

    exclude("META-INF/maven/**")
    exclude("META-INF/versions/**")

    exclude("org/junit/**")
}

kotlin {
    jvmToolchain(21)
}