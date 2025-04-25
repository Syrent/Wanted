plugins {
    id("java")
    id("maven-publish")
    id("com.gradle.shadow") version "9.0.0-beta.12"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        url = uri("https://repo.codemc.org/repository/maven-public")
    }
    maven {
        url = uri("https://repo.extendedclip.com/releases/")
    }
    maven {
        url = uri("https://maven.citizensnpcs.co/repo")
    }
    maven {
        url = uri("https://maven.enginehub.org/repo/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://nexus.iridiumdevelopment.net/repository/maven-releases/")
    }
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.bstats:bstats-bukkit:3.0.0")
    implementation("com.iridium:IridiumColorAPI:1.0.9")
    implementation("com.github.cryptomorin:XSeries:13.2.0")

    compileOnly("org.spigotmc:spigot-api:1.19-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("net.citizensnpcs:citizens-main:2.0.35-SNAPSHOT") {
        exclude(group = "*", module = "*")
    }
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.5")
    compileOnly("com.mojang:authlib:1.5.21")
}

group = "org.sayandev"
version = "2.7.0"
description = "Wanted"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val targetJavaVersion = 8

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    compileJava {
        sourceCompatibility = "1.$targetJavaVersion"
        targetCompatibility = "1.$targetJavaVersion"
        options.encoding = "UTF-8"
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.shadowJar {
    relocate("org.bstats", "${group}.bstats")
}