package com.maraquya.staticCodeAnalysis

import com.maraquya.virtualization.Docker

class SonarQube implements Serializable, StaticCodeAnalysis {
    private String dockerImage = "sonarsource/sonar-scanner-cli:4.4"
    private Docker docker
    private String absoluteProjectPath
    private script

    SonarQube(script, Docker docker, String absoluteProjectPath) {
        this.script = script
        this.docker = docker
        this.absoluteProjectPath = absoluteProjectPath
    }

    void run() {
        this.script.steps.withCredentials([
            this.script.string(credentialsId: 'sonar', variable: 'SONAR_TOKEN')
        ]) {
            this.docker.run(
                dockerImage,
                "sonar-scanner -Dsonar.login=${this.script.env.SONAR_TOKEN}",
                "-e SRC_PATH=\"/app\" -e SONAR_HOST_URL=${this.script.env.SONAR_HOST_URL} " +
                    "-v ${this.absoluteProjectPath}:/app -w /app"
            );
        }
    }
}
