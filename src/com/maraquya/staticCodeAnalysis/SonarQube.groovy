package com.maraquya.staticCodeAnalysis

import com.maraquya.virtualization.Docker

class SonarQube implements Serializable, StaticCodeAnalysis {
    private String dockerImage = "sonarsource/sonar-scanner-cli:4.5"
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
            this.executeSonarScanner()
            this.checkQualityGate()
        }
    }

    private void executeSonarScanner() {
        if (this.script.env.SONAR_SERVER_NAME == null) {
            throw new Exception("You must register the \"SONAR_SERVER_NAME\" variable")
        }
        this.script.withSonarQubeEnv(this.script.env.SONAR_SERVER_NAME) {
            this.guardConfigServer()
            this.docker.run(
                dockerImage,
                "sonar-scanner -X -Dsonar.login=${this.script.env.SONAR_AUTH_TOKEN}",
                "-e SRC_PATH=\"/app\" -e SONAR_HOST_URL=${this.script.env.SONAR_HOST_URL} " +
                    "-v ${this.absoluteProjectPath}:/app -w /app"
            )
        }
    }

    private void guardConfigServer() {
        if (this.script.env.SONAR_AUTH_TOKEN == null) {
            throw new Exception("You must register the \"Server authentication token\" - \"${this.script.env.SONAR_SERVER_NAME}\"")
        }
        if (this.script.env.SONAR_HOST_URL == null) {
            throw new Exception("You must register the \"Server url\" - \"${this.script.env.SONAR_SERVER_NAME}\"")
        }
    }

    private void checkQualityGate() {
        this.script.timeout(time: 1, unit: 'HOURS') {
            def qualityGate = this.script.waitForQualityGate()
            if (qualityGate.status != 'OK') {
                throw new Exception("Pipeline aborted due to quality gate failure: ${qualityGate.status}")
            }
        }
    }
}
