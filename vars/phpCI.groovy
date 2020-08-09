import com.maraquya.packageManager.Composer
import com.maraquya.virtualization.Docker
import com.maraquya.staticCodeAnalysis.StaticCodeAnalysis

void call(script, String dockerImage = null) {
    Docker docker = new Docker(script)
    Composer composer = new Composer(script, docker)
    StaticCodeAnalysis staticCodeAnalysis = new StaticCodeAnalysis(script, docker)
    if (dockerImage) {
        composer.dockerImage = dockerImage
    }

    continuousIntegration(composer, staticCodeAnalysis)
}
