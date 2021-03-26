package com.maraquya.staticCodeAnalysis

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import com.maraquya.virtualization.Docker
import org.jenkinsci.plugins.workflow.cps.CpsScript

class SonarQubeSpec extends JenkinsPipelineSpecification {
    private SonarQube sonarQube
    private Docker docker
    private CpsScript cpsScript

    void setup() {
        this.docker = Mock(Docker)
        CpsScript cpsScript = getPipelineMock("CpsScript")
        explicitlyMockPipelineVariable("steps")
        this.sonarQube = new SonarQube(cpsScript, this.docker, "/workspace")
    }

    void "execute run with credential"() {
        when:
        this.sonarQube.run()

        then:
        1 * getPipelineMock("steps.withCredentials").call(_)
        1 * getPipelineMock("string.call")
            .call(['credentialsId':'sonar', 'variable':'SONAR_TOKEN'])
        2 * getPipelineMock("env.getProperty").call('SONAR_TOKEN') >> "sonarToken"
        2 * getPipelineMock("env.getProperty").call('SONAR_HOST_URL') >> "sonatHostUrl"
        1 * this.docker.run(
            'sonarsource/sonar-scanner-cli:4.4',
            'sonar-scanner -Dsonar.login=sonarToken',
            '-e SRC_PATH="/app" -e SONAR_HOST_URL=sonatHostUrl -v /workspace:/app -w /app'
        )
    }

    void "execute run without credential"() {
        when:
        this.sonarQube.run()
        then:
        1 * getPipelineMock("steps.withCredentials").call(_)
        1 * getPipelineMock("string.call")
            .call(['credentialsId':'sonar', 'variable':'SONAR_TOKEN'])
        1 * getPipelineMock("env.getProperty").call('SONAR_TOKEN')
        Exception e = thrown()
        assert e.message == "You must register the \"sonar\" credential"
    }

    void "execute run without sonar host variable"() {
        when:
        this.sonarQube.run()
        then:
        1 * getPipelineMock("steps.withCredentials").call(_)
        1 * getPipelineMock("string.call")
            .call(['credentialsId':'sonar', 'variable':'SONAR_TOKEN'])
        1 * getPipelineMock("env.getProperty").call('SONAR_TOKEN') >> "sonarToken"
        1 * getPipelineMock("env.getProperty").call('SONAR_HOST_URL')
        Exception e = thrown()
        assert e.message == "You must register the \"SONAR_HOST_URL\" variable"
    }
}
