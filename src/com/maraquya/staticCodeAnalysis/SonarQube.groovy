package com.maraquya.staticCodeAnalysis

import com.maraquya.virtualization.Docker

class SonarQube implements Serializable, StaticCodeAnalysis {
    private String dockerImage = "sonarsource/sonar-scanner-cli:4.3"
    private String projectPath = 'app'
    private Docker docker
    private script

    SonarQube(script, Docker docker) {
        this.script = script
        this.docker = docker
    }

    void run() {
        this.script.steps.withCredentials([
            this.script.string(credentialsId: 'sonar', variable: 'SONAR_TOKEN')
        ]) {
            this.docker.run(
                dockerImage,
                "-Dsonar.login=${this.script.env.SONAR_TOKEN}",
                "--user=\"\$(id -u):\$(id -g)\" " +
                    "-e SRC_PATH=\"/app\" -e SONAR_HOST_URL=${this.script.env.SONAR_HOST_URL}" +
                    "-v ${this.script.env.WORKSPACE}/${this.projectPath}:/app " +
                    "-w /app"
            );
        }
    }
}
