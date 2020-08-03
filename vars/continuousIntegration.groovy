import com.maraquya.packageManager.PackageManager

void call(PackageManager packageManager) {
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
    }
}

