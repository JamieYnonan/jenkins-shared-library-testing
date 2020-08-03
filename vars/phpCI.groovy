import com.maraquya.packageManager.Composer
import com.maraquya.virtualization.Docker

void call(script, String dockerImage = null) {
    Docker docker = new Docker(script)
    Composer composer = new Composer(script, docker)
    if (dockerImage) {
        composer.dockerImage = dockerImage
    }

    continuousIntegration composer
}
