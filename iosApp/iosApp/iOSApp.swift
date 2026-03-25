import SwiftUI
import SharedApp

@main
struct iOSApp: App {
    init() {
        
        AppLogger.shared.initialize(logger: iOSLogger())
        
        IosDependencyProvider.shared.initializeKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
