package com.maraquya.virtualization

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import org.jenkinsci.plugins.workflow.cps.CpsScript

class DockerSpec extends JenkinsPipelineSpecification {
    private Docker docker

    void setup() {
        CpsScript cpsScript = getPipelineMock("CpsScript")
        this.docker = new Docker(cpsScript)
    }

    void "run without flags"() {
        when:
        this.docker.run("image", "command")

        then:
        1 * getPipelineMock("sh")
            .call('docker run --rm --user="$(id -u):$(id -g)"  image command')
    }

    void "run with flags"() {
        when:
        this.docker.run("image", "command", "flags")

        then:
        1 * getPipelineMock("sh")
            .call('docker run --rm --user="$(id -u):$(id -g)" flags image command')
    }
}
