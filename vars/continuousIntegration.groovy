import com.maraquya.packageManager.PackageManager
import com.maraquya.staticCodeAnalysis.StaticCodeAnalysis

void call(PackageManager packageManager, StaticCodeAnalysis staticCodeAnalysis) {
    node {
        properties([buildDiscarder(logRotator(numToKeepStr: '2'))])

        stage("Checkout") {
            checkout scm
        }

        stage("Install") {
            packageManager.install()
        }

        stage("Check Style") {
            packageManager.checkStyle()
        }

        stage("Unit Test") {
            packageManager.unitTest()
        }

        stage("Static Code Analysis") {
            staticCodeAnalysis.run()
        }
    }
}
