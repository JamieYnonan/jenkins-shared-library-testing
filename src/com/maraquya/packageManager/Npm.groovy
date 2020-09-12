package com.maraquya.packageManager

import com.maraquya.virtualization.Docker

class Npm implements PackageManager {
    private String dockerImage = "jamieynonan/nodechrome:0.1.0-alpine"
    private Docker docker
    private String absoluteProjectPath
    private script

    Npm(script, Docker docker, String absoluteProjectPath) {
        this.script = script
        this.docker = docker
        this.absoluteProjectPath = absoluteProjectPath
    }

    void setDockerImage(String dockerImage) {
        this.dockerImage = dockerImage
    }

    void install() {
        this.execCommand("npm install --no-optional --no-package-lock --only=dev")
    }

    private void execCommand(final String command) {
        this.docker.run(
            this.dockerImage,
            command,
            "-v ${this.absoluteProjectPath}:/app -w /app"
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
