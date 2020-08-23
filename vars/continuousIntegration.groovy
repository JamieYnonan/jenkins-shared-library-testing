import com.maraquya.packageManager.PackageManager
import com.maraquya.staticCodeAnalysis.StaticCodeAnalysis
import com.maraquya.ContinuousIntegration

void call(PackageManager packageManager, StaticCodeAnalysis staticCodeAnalysis) {
    echo "script"
    echo ${script}
    echi "this"
    echo ${this}
    ContinuousIntegration continuousIntegration = new ContinuousIntegration(
        packageManager,
        staticCodeAnalysis
    )

    node {
        continuousIntegration.execute()
    }
}
