package com.maraquya.packageManager

import com.maraquya.virtualization.Docker

class Composer implements PackageManager, Serializable {
    private static final String INSTALL_FLAGS = "--no-ansi --no-cache --no-interaction " +
        "--no-progress --prefer-dist --optimize-autoloader"
    private String dockerImage = "composer:latest"
    private Docker docker
    private String absoluteProjectPath
    private script

    Composer(script, Docker docker, String absoluteProjectPath) {
        this.script = script
        this.docker = docker
        this.absoluteProjectPath = absoluteProjectPath
    }

    void setDockerImage(String dockerImage) {
        this.dockerImage = dockerImage
    }

    void install() {
        this.execCommand("composer i ${INSTALL_FLAGS}")
    }

    private void execCommand(final String command) {
        this.docker.run(
            this.dockerImage,
            command,
            "-v ${this.absoluteProjectPath}:/app"
        )
    }

    void unitTest() {
        this.execCommand("composer test")
    }

    void checkStyle() {
        this.execCommand("composer check-style")
    }

    void mutationTest() {
        this.execCommand("composer mutation")
    }
}
