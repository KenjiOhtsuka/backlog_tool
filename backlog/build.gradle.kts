plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
    jcenter()
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    implementation("com.nulab-inc:backlog4j:2.3.3")
    implementation("com.github.KenjiOhtsuka:DateTuner:0.0.9")
}