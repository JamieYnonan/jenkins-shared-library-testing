import com.maraquya.packageManager.Npm
import com.maraquya.virtualization.Docker
import com.maraquya.staticCodeAnalysis.SonarQube
import com.maraquya.ContinuousIntegration

void call(String projectPath = '', String dockerImage = null) {
    node {
        Docker docker = new Docker(this)
        Npm npm = new Npm(this, docker, "${env.WORKSPACE}/${projectPath}")
        if (dockerImage) {
            npm.setDockerImage(dockerImage)
        }
        SonarQube sonarQube = new SonarQube(this, docker, "${env.WORKSPACE}/${projectPath}")

        ContinuousIntegration continuousIntegration = new ContinuousIntegration(this, npm, sonarQube)
        continuousIntegration.execute()
    }
}
