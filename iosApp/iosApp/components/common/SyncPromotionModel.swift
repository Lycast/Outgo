import SwiftUI
import SharedApp

struct SyncPromotionModal: ViewModifier {
    @Binding var isPresented: Bool
    let onNavigateToLogin: () -> Void
    
    func body(content: Content) -> some View {
        content
            .alert(HeaderLabels.shared.SYNC_PROMO_TITLE, isPresented: $isPresented) {
                // Bouton de confirmation
                Button(HeaderLabels.shared.SYNC_PROMO_ACTION_LOGIN) {
                    onNavigateToLogin()
                }
                
                // Bouton d'annulation
                Button(HeaderLabels.shared.SYNC_PROMO_ACTION_LATER, role: .cancel) {
                    // Fermé par défaut
                }
            } message: {
                Text(HeaderLabels.shared.SYNC_PROMO_DESC)
            }
    }
}

extension View {
    func syncPromotionModal(isPresented: Binding<Bool>, onNavigateToLogin: @escaping () -> Void) -> some View {
        self.modifier(SyncPromotionModal(isPresented: isPresented, onNavigateToLogin: onNavigateToLogin))
    }
}
