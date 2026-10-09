// Core module
plugins {
    id("com.gradleup.shadow") version "9.6.1"
}

group = "com.github.fragenna2.multiversion"
version = "1.0.0"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

//    compileOnly(project(":support_v1_8"))
//    compileOnly(project(":support_26_2"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
    from(project(":support_v1_8").the<SourceSetContainer>()["main"].output)
    from(project(":support_26_2").the<SourceSetContainer>()["main"].output)
    from(project(":support_v1_21").the<SourceSetContainer>()["main"].output)

    dependsOn(":support_v1_8:classes", ":support_26_2:classes")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}