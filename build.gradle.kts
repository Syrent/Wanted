plugins {
    id("java")
    id("maven-publish")
    id("com.gradleup.shadow") version "9.0.0-beta12"
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

    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set(project.name)
        destinationDirectory.set(file("./bin/"))

        relocate("org.bstats", "${group}.bstats")
    }

    jar {
        enabled = false
    }

    build {
        dependsOn(clean)
        dependsOn(shadowJar)
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

    repositories {
        maven {
            name = "sayandevelopment-repo"
            url = uri(if (version.toString().contains("-SNAPSHOT")) "https://repo.sayandev.org/snapshots/" else "https://repo.sayandev.org/releases/")

            credentials {
                username = System.getenv("REPO_SAYAN_USER") ?: project.findProperty("repo.sayan.user") as? String
                password = System.getenv("REPO_SAYAN_TOKEN") ?: project.findProperty("repo.sayan.token") as? String
            }
        }
    }
}