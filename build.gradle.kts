plugins {
    id("java")
}

group = "com.github.khiemnguyen15"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}
tasks.test {
    useJUnitPlatform()
    jvmArgs("--enable-preview")
}

dependencies {
    implementation("com.discord4j:discord4j-core:3.2.6")
    implementation("org.postgresql:postgresql:42.7.3")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}