import org.gradle.api.JavaVersion.VERSION_1_8

apply<JavaPlugin>()

group = "iReport"
version = "2.0.1-SNAPSHOT"

configure<JavaPluginConvention> {
	targetCompatibility = VERSION_1_8
	sourceCompatibility = VERSION_1_8

}
 
repositories {
    mavenCentral()
	maven {
		setUrl("http://repo.spongepowered.org/maven/")
	}
}
 
dependencies {
    implementation ("org.spongepowered:spongeapi:6.0.0-SNAPSHOT")
}