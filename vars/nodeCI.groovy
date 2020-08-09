import com.maraquya.packageManager.Npm
import com.maraquya.virtualization.Docker
import com.maraquya.staticCodeAnalysis.StaticCodeAnalysis

void call(script, String dockerImage = null) {
    Docker docker = new Docker(script)
    Npm npm = new Npm(script, docker)
    StaticCodeAnalysis staticCodeAnalysis = new StaticCodeAnalysis(script, docker)
    if (dockerImage) {
        npm.dockerImage = dockerImage
    }

    continuousIntegration(npm, staticCodeAnalysis)
}
