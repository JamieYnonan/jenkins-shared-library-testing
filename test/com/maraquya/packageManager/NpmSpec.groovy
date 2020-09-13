package com.maraquya.packageManager

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import com.maraquya.virtualization.Docker
import org.jenkinsci.plugins.workflow.cps.CpsScript

class NpmSpec extends JenkinsPipelineSpecification {
    private Npm npm
    private Docker docker

    void setup() {
        this.docker = Mock(Docker)
        CpsScript cpsScript = getPipelineMock("CpsScript")
        this.npm = new Npm(cpsScript, this.docker, "/workspace")
    }

    void "install execute with default image"() {
        when:
        this.npm.install()

        then:
        1 * this.docker.run(
            'jamieynonan/nodechrome:0.1.0-alpine',
            'npm install --no-optional --no-package-lock --only=dev',
            '-v /workspace:/app -w /app'
        )
    }

    void "install execute with other"() {
        when:
        this.npm.setDockerImage("other:image")
        this.npm.install()

        then:
        1 * this.docker.run(
            'other:image',
            'npm install --no-optional --no-package-lock --only=dev',
            '-v /workspace:/app -w /app'
        )
    }

    void "checkStyle execute"() {
        when:
        this.npm.checkStyle()

        then:
        1 * this.docker.run(
            'jamieynonan/nodechrome:0.1.0-alpine',
            'npm run check-style',
            '-v /workspace:/app -w /app'
        )
    }

    void "unitTest execute"() {
        when:
        this.npm.unitTest()

        then:
        1 * this.docker.run(
            'jamieynonan/nodechrome:0.1.0-alpine',
            'npm test',
            '-v /workspace:/app -w /app'
        )
    }

    void "mutationTest execute"() {
        when:
        this.npm.mutationTest()

        then:
        1 * this.docker.run(
            'jamieynonan/nodechrome:0.1.0-alpine',
            'npm mutation',
            '-v /workspace:/app -w /app'
        )
    }
}
