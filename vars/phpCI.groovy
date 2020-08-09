import com.maraquya.packageManager.Composer
import com.maraquya.virtualization.Docker
import com.maraquya.staticCodeAnalysis.SonarQube

void call(script, String dockerImage = null) {
    Docker docker = new Docker(script)
    Composer composer = new Composer(script, docker)
    SonarQube sonarQube = new SonarQube(script, docker)
    if (dockerImage) {
        composer.dockerImage = dockerImage
    }

    continuousIntegration(composer, sonarQube)
}
