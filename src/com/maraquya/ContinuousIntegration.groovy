package com.maraquya

import com.maraquya.packageManager.PackageManager
import com.maraquya.staticCodeAnalysis.StaticCodeAnalysis

class ContinuousIntegration implements Serializable {
    private PackageManager packageManager
    private StaticCodeAnalysis staticCodeAnalysis
    private script

    ContinuousIntegration(
        script,
        PackageManager packageManager,
        StaticCodeAnalysis staticCodeAnalysis
    ) {
        this.script = script
        this.packageManager = packageManager
        this.staticCodeAnalysis = staticCodeAnalysis
    }

    void execute() {
        this.script.properties(
            [this.script.buildDiscarder(this.script.logRotator(numToKeepStr: '2'))]
        )

        this.script.stage("Checkout") {
            this.script.checkout this.script.scm
        }

        this.script.stage("Install") {
            this.packageManager.install()
        }

        this.script.stage("Check Style") {
            this.packageManager.checkStyle()
        }

        this.script.stage("Unit Test") {
            this.packageManager.unitTest()
        }

        this.script.stage("Static Code Analysis") {
            this.staticCodeAnalysis.run()
        }
    }
}