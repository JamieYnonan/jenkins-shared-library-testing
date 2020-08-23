import com.maraquya.packageManager.Npm
import com.maraquya.virtualization.Docker
import com.maraquya.staticCodeAnalysis.SonarQube
import com.maraquya.ContinuousIntegration

void call(script, String dockerImage = null) {
    Docker docker = new Docker(script)
    Npm npm = new Npm(script, docker)
    SonarQube sonarQube = new SonarQube(script, docker)
    if (dockerImage) {
        npm.dockerImage = dockerImage
    }
    ContinuousIntegration continuousIntegration = new ContinuousIntegration(script, npm, sonarQube)
    node {
        continuousIntegration.execute()
    }
}
