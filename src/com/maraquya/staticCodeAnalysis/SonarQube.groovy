package com.maraquya.staticCodeAnalysis

import com.maraquya.virtualization.Docker

class SonarQube implements Serializable {
    private String dockerImage = "sonarsource/sonar-scanner-cli:4.3"
    private String projectPath = 'app'
    private Docker docker
    private script

    SonarQube(script, Docker docker) {
        this.script = script
        this.docker = docker
    }

    void runner() {
        this.script.steps.withCredentials([
            this.script.string(credentialsId: 'sonar', variable: 'SONAR_TOKEN')
        ]) {
            this.docker.run(
                dockerImage,
                "-Dsonar.login=${script.env.SONAR_TOKEN} -D",
                "--user=\"\$(id -u):\$(id -g)\" " +
                    "-e SRC_PATH=\"/app\" " +
                    "-v ${this.script.env.WORKSPACE}/${this.projectPath}:/app " +
                    "-w /app"
            );
        }
    }
}
