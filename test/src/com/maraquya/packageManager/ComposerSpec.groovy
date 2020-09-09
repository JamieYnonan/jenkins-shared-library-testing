import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import com.maraquya.packageManager.Composer
import com.maraquya.virtualization.Docker
import org.jenkinsci.plugins.workflow.cps.CpsScript

class ComposerSpec extends JenkinsPipelineSpecification {
    private Composer composer
    private Docker docker

    void setup() {
        this.docker = Mock(Docker)
        CpsScript cpsScript = getPipelineMock("CpsScript")
        this.composer = new Composer(cpsScript, this.docker, "/workspace")
    }

    void "install execute with default image"() {
        when:
        this.composer.install()

        then:
        1 * this.docker.run(
            'composer:latest',
            'composer i --no-ansi --no-cache --no-interaction --no-progress --prefer-dist --optimize-autoloader',
            '-v /workspace:/app'
        )
    }

    void "install execute with other"() {
        when:
        this.composer.setDockerImage("other:image")
        this.composer.install()

        then:
        1 * this.docker.run(
            'other:image',
            'composer i --no-ansi --no-cache --no-interaction --no-progress --prefer-dist --optimize-autoloader',
            '-v /workspace:/app'
        )
    }

    void "unitTest execute"() {
        when:
        this.composer.unitTest()

        then:
        1 * this.docker.run(
            'composer:latest',
            'composer test',
            '-v /workspace:/app'
        )
    }

    void "checkStyle execute"() {
        when:
        this.composer.checkStyle()

        then:
        1 * this.docker.run(
            'composer:latest',
            'composer check-style',
            '-v /workspace:/app'
        )
    }

    void "mutationTest execute"() {
        when:
        this.composer.mutationTest()

        then:
        1 * this.docker.run(
            'composer:latest',
            'composer mutation',
            '-v /workspace:/app'
        )
    }
}
