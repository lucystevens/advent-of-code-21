import java.time.LocalDate
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

plugins {
    kotlin("jvm") version "1.6.0"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "7.3"
    }
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.squareup.okhttp3:okhttp:4.9.3")
    }
}

fun getInput(day: Int): String {
    val client = OkHttpClient().newBuilder()
        .build()
    val request = Request.Builder()
        .url("https://adventofcode.com/2021/day/$day/input")
        .get()
        .addHeader("Cookie", "session=${properties["SESSION_COOKIE"]}" )
        .build()
    val response: Response = client.newCall(request).execute()
    if(response.code != 200){
        throw UncheckedIOException("Could not fetch challenge input. Status code: ${response.code}. Body: ${response.body?.string()}")
    }
    return response.body?.string() ?: ""
}

tasks.register("setup") {
    doLast {
        println(properties["SESSION_COOKIE"])
        val day = LocalDate.now().dayOfMonth
        val paddedDay = day.toString().padStart(2, '0')
        val newDirectory = File("src/day$paddedDay")
        if(newDirectory.exists()){
            throw IllegalArgumentException("There already exists a directory for day$paddedDay")
        }

        File("src/template").copyRecursively(newDirectory)
        val templateKotlinFile = File(newDirectory, "template.kt")
        val kotlinContents = templateKotlinFile.readText()
        File(newDirectory, "Day$paddedDay.kt").apply {
            createNewFile()
            writeText(kotlinContents.replace("template", "day$paddedDay"))
        }
        templateKotlinFile.delete()

        val input = getInput(day)
        val inputFile = File(newDirectory, "input.txt")
        inputFile.writeText(input.trim())

    }
}

tasks.register("getDayOfMonth")  {
    doLast {
        File(System.getenv("GITHUB_ENV"))
            .appendText("\nDAY_OF_MONTH=${LocalDate.now().dayOfMonth}")
    }
}
