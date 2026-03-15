import SwiftUI
import SharedApp

struct DashboardScreen: View {
    // 1. On reçoit le presenter KMP
    let presenter: OutgoingPresenter
    let onNavigateToSettings: () -> Void
    
    // 2. On prépare une variable pour stocker l'état (avec une valeur initiale par défaut)
    @State private var state: OutgoingState = OutgoingState(
        isLoading: false, outgoings: [], currentDay: 0, currentMonth: 0, selectedMonth: 0,
        monthlyIncomeInCents: 0, totalOutgoingsInCents: 0, disposableIncomeInCents: 0,
        remainingToPayInCents: 0, isCloudSyncActive: false, error: nil, isHeroExpanded: true
    )
    
    var body: some View {
        VStack(spacing: 20) {
            
            // --- LECTURE DE L'ÉTAT (State) ---
            
            if state.isLoading {
                ProgressView("Chargement des données...")
            } else {
                Text("Budget Restant : \(state.disposableIncomeInCents.uiAmount)")
                    .font(.title)
                    .bold()
                
                Text("Vous avez \(state.outgoings.count) dépenses prévues.")
            }
            
            // --- ENVOI D'INTENTIONS (Intent) ---
            
            Button("Simuler la suppression de la dépense 1") {
                // On utilise la classe Kotlin générée pour l'Intent Delete
                presenter.onIntent(intent: OutgoingIntentDelete(id: "1"))
            }
            .buttonStyle(.borderedProminent)
            
            Button("Aller aux paramètres") {
                onNavigateToSettings()
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .task {
            // 3. LA MAGIE SKIE : On écoute le StateFlow de Kotlin en temps réel !
            for await newState in presenter.state {
                self.state = newState
            }
        }
    }
}
