import com.maraquya.packageManager.PackageManager
import com.maraquya.staticCodeAnalysis.StaticCodeAnalysis
import com.maraquya.ContinuousIntegration

void call(PackageManager packageManager, StaticCodeAnalysis staticCodeAnalysis) {
    ContinuousIntegration continuousIntegration = new ContinuousIntegration(
        this,
        packageManager,
        staticCodeAnalysis
    )

    node {
        continuousIntegration.execute()
    }
}
