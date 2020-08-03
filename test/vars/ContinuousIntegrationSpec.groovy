import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import com.maraquya.packageManager.PackageManager

class ContinuousIntegrationSpec extends JenkinsPipelineSpecification {
    def continuousIntegrationVar = null
    private PackageManager packageManager = Mock()

    void setup() {
        continuousIntegrationVar = loadPipelineScriptForTest("vars/continuousIntegration.groovy")
        continuousIntegrationVar.getBinding().setVariable("scm", null)
        explicitlyMockPipelineStep('properties')
    }

    void "[continuousIntegration] logRotator numToKeep"() {
        when:
        continuousIntegrationVar(this.packageManager)

        then:
        1 * getPipelineMock("logRotator.call")(['numToKeepStr':'2'])
    }

    void "[continuousIntegration] stages"() {
        when:
        continuousIntegrationVar(this.packageManager)

        then:
        1 * this.getPipelineMock("node").call(_)
        1 * this.getPipelineMock("stage")(_) >> stageName("Checkout")
        1 * this.getPipelineMock("stage")(_) >> stageName("Install")
        1 * this.getPipelineMock("stage")(_) >> stageName("Check Style")
        1 * this.getPipelineMock("stage")(_) >> stageName("Unit Test")
    }

    private static Closure stageName(String name) {
        return { _arguments ->
            assert name == _arguments[0]
        }
    }

    void "[continuousIntegration] stage Checkout"() {
        when:
        continuousIntegrationVar(this.packageManager)

        then:
        1 * getPipelineMock("checkout")(_)
    }

    void "[continuousIntegration] stage Install"() {
        when:
        continuousIntegrationVar(this.packageManager)

        then:
        1 * this.packageManager.install()
    }

    void "[continuousIntegration] stage Check Style"() {
        when:
        continuousIntegrationVar(this.packageManager)

        then:
        1 * this.packageManager.checkStyle()
    }

    void "[continuousIntegration] stage Unit Test"() {
        when:
        continuousIntegrationVar(this.packageManager)

        then:
        1 * this.packageManager.unitTest()
    }
}
