import com.maraquya.packageManager.Npm
import com.maraquya.virtualization.Docker
import com.maraquya.staticCodeAnalysis.SonarQube
import com.maraquya.ContinuousIntegration

void call(String projectPath = '', String dockerImage = null) {
    Docker docker = new Docker(this)
    Npm npm = new Npm(this, docker)
    npm.setProjectPath(projectPath)
    if (dockerImage) {
        npm.setDockerImage(dockerImage)
    }
    SonarQube sonarQube = new SonarQube(this, docker)

    ContinuousIntegration continuousIntegration = new ContinuousIntegration(this, npm, sonarQube)
    node {
        continuousIntegration.execute()
    }
}
