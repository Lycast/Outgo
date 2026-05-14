import SwiftUI
import SharedApp

struct ContentView: View {
    let coordinator = IosDependencyProvider.shared.navCoordinator

    @State private var currentStep: AppStep = AppStepSplash.shared

    var body: some View {
        VStack(spacing: 20) {
            Text("🚀 Outgo iOS")
                .font(.largeTitle)
                .bold()
            
            Text("Écran actuel demandé par KMP :")
                .foregroundColor(.gray)
            
            Text(String(describing: currentStep))
                .font(.headline)
                .foregroundColor(.blue)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(UIColor.systemBackground))
        .task {
            // 3. Grâce à SKIE, on écoute le StateFlow KMP comme un flux Swift !
            for await navState in coordinator.state {
                withAnimation {
                    self.currentStep = navState.currentStep
                }
            }
        }
    }
}

#Preview {
    ContentView()
}
