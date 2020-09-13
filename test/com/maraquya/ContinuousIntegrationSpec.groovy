package com.maraquya

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import com.maraquya.packageManager.PackageManager
import com.maraquya.staticCodeAnalysis.StaticCodeAnalysis

class ContinuousIntegrationSpec extends JenkinsPipelineSpecification {
    private ContinuousIntegration continuousIntegration
    private PackageManager packageManager
    private StaticCodeAnalysis staticCodeAnalysis

    void setup() {
        this.packageManager = Mock(PackageManager)
        this.staticCodeAnalysis = Mock(StaticCodeAnalysis)
        continuousIntegration = new ContinuousIntegration(
            getPipelineMock("CpsScript"),
            this.packageManager,
            this.staticCodeAnalysis
        )
        explicitlyMockPipelineVariable("scm")
        explicitlyMockPipelineStep('properties')
    }

    void "[continuousIntegration] logRotator numToKeep"() {
        when:
        node {
            this.continuousIntegration.execute()
        }

        then:
        1 * getPipelineMock("logRotator.call")(['numToKeepStr':'2'])
    }

    void "[continuousIntegration] stages"() {
        when:
        node {
            this.continuousIntegration.execute()
        }

        then:
        1 * this.getPipelineMock("node").call(_)
        1 * this.getPipelineMock("stage")(_) >> stageName("Checkout")
        1 * this.getPipelineMock("stage")(_) >> stageName("Install")
        1 * this.getPipelineMock("stage")(_) >> stageName("Check Style")
        1 * this.getPipelineMock("stage")(_) >> stageName("Unit Test")
        1 * this.getPipelineMock("stage")(_) >> stageName("Static Code Analysis")
    }

    private static Closure stageName(String name) {
        return { _arguments ->
            assert name == _arguments[0]
        }
    }

    void "[continuousIntegration] stage Checkout"() {
        when:
        node {
            this.continuousIntegration.execute()
        }

        then:
        1 * getPipelineMock("checkout")(_)
    }

    void "[continuousIntegration] stage Install"() {
        when:
        node {
            this.continuousIntegration.execute()
        }

        then:
        1 * this.packageManager.install()
    }

    void "[continuousIntegration] stage Check Style"() {
        when:
        node {
            this.continuousIntegration.execute()
        }

        then:
        1 * this.packageManager.checkStyle()
    }

    void "[continuousIntegration] stage Unit Test"() {
        when:
        node {
            this.continuousIntegration.execute()
        }

        then:
        1 * this.packageManager.unitTest()
    }

    void "[continuousIntegration] stage Static Code Analysis"() {
        when:
        node {
            this.continuousIntegration.execute()
        }

        then:
        1 * this.staticCodeAnalysis.run()
    }
}
