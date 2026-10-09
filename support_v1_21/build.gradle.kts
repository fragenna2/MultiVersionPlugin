// Module 26_2
plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.23"
}

group = "com.github.fragenna2.multiversion"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation(project(":core"))
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")
}
