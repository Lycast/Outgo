import SwiftUI
import SharedApp // Votre framework Kotlin

struct ContentView: View {
    @State private var showContent = false
    
    // 1. Récupération du Presenter depuis Koin via votre Provider
    let presenter = IosDependencyProvider.shared.outgoingPresenter

    var body: some View {
        VStack {
            Button("Click me!") {
                withAnimation {
                    showContent = !showContent
                }
            }

            if showContent {
                VStack(spacing: 16) {
                    Image(systemName: "swift")
                        .font(.system(size: 200))
                        .foregroundColor(.accentColor)
                    
                    // 2. On remplace le Greeting.
                    // Ici, j'affiche simplement la description de l'objet pour vérifier qu'il n'est pas nul.
                    // Par la suite, vous pourrez appeler les variables d'état de votre Presenter.
                    Text("Presenter chargé : \(String(describing: presenter))")
                }
                .transition(.move(edge: .top).combined(with: .opacity))
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
