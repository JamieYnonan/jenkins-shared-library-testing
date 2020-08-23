import com.maraquya.packageManager.Composer
import com.maraquya.virtualization.Docker
import com.maraquya.staticCodeAnalysis.SonarQube
import com.maraquya.ContinuousIntegration

void call(String projectPath = '', String dockerImage = null) {
    Docker docker = new Docker(this)
    Composer composer = new Composer(this, docker)
    composer.setProjectPath(projectPath)
    if (dockerImage) {
        composer.setDockerImage(dockerImage)
    }
    SonarQube sonarQube = new SonarQube(this, docker)
    sonarQube.setProjectPath(projectPath)

    ContinuousIntegration continuousIntegration = new ContinuousIntegration(this, composer, sonarQube)

    node {
        continuousIntegration.execute()
    }
}
