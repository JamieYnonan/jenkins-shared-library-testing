import com.maraquya.packageManager.Npm
import com.maraquya.virtualization.Docker

void call(script, String dockerImage = null) {
    Docker docker = new Docker(script)
    Npm npm = new Npm(script, docker)
    if (dockerImage) {
        npm.dockerImage = dockerImage
    }

    continuousIntegration npm
}
