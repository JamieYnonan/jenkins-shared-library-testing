package com.maraquya.packageManager

import com.maraquya.virtualization.Docker

class Composer implements PackageManager, Serializable {
    private String projectPath = 'app'
    private String dockerImage = "composer:latest"
    private Docker docker
    private script

    Composer(script, Docker docker) {
        this.script = script
        this.docker = docker
    }

    void install() {
        this.execCommand("composer install --no-ansi --no-cache --no-interaction")
    }

    private void execCommand(final String command) {
        this.docker.run(
            this.dockerImage,
            command,
            "-v ${this.script.env.WORKSPACE}/${this.projectPath}:/app"
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
