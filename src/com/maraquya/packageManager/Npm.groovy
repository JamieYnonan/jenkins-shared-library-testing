package com.maraquya.packageManager

import com.maraquya.virtualization.Docker

class Npm implements PackageManager {
    private String projectPath = ''
    private String dockerImage = "jamieynonan/nodechrome:0.1.0-alpine"
    private Docker docker
    private script

    Npm(script, Docker docker) {
        this.script = script
        this.docker = docker
    }

    void setProjectPath(String projectPath) {
        this.projectPath = projectPath
    }

    void setDockerImage(String dockerImage) {
        this.dockerImage = dockerImage
    }

    void install() {
        this.execCommand("npm install")
    }

    private void execCommand(final String command) {
        this.docker.run(
            this.dockerImage,
            command,
            "-v ${this.script.env.WORKSPACE}/${this.projectPath}:/app -w /app"
        )
    }

    void checkStyle() {
        this.execCommand("npm run check-style")
    }

    void unitTest() {
        this.execCommand("npm test")
    }

    void mutationTest() {
        this.execCommand("npm mutation")
    }
}
