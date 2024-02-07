plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "AirQualityMonitoring"
include("components:data-component")
findProject(":components:data-component")?.name = "data-component"
