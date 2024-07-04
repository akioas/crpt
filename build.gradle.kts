plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.8.9")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.8.9")

}
