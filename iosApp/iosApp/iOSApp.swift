import SwiftUI
import SharedApp

@main
struct iOSApp: App {
    init() {
        IosDependencyProvider.shared.initializeKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
