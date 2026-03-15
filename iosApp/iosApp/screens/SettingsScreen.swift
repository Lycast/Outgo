import SwiftUI

struct SettingsScreen: View {
    // Les propriétés exigées par ContentView
    var isDarkMode: Bool
    let onNavigateBack: () -> Void
    let onToggleDarkMode: (Bool) -> Void
    let onCoffeeClick: () -> Void
    let onTipsClick: () -> Void
    let onContactClick: () -> Void
    
    var body: some View {
        VStack(spacing: 30) {
            Text("Écran Paramètres")
                .font(.largeTitle)
                .bold()
            
            // Un vrai Toggle SwiftUI branché sur vos callbacks
            Toggle("Mode Sombre", isOn: Binding(
                get: { isDarkMode },
                set: { newValue in onToggleDarkMode(newValue) }
            ))
            .padding(.horizontal, 40)
            
            Button(action: onNavigateBack) {
                Text("Retour au Dashboard")
                    .padding()
                    .background(Color.red)
                    .foregroundColor(.white)
                    .cornerRadius(8)
            }
            
            // Boutons factices pour tester les liens
            HStack(spacing: 15) {
                Button("Café", action: onCoffeeClick)
                Button("Site", action: onTipsClick)
                Button("Contact", action: onContactClick)
            }
            .buttonStyle(.bordered)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}
