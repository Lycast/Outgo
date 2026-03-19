import SwiftUI
import SharedApp

struct SyncPromotionModal: ViewModifier {
    @Binding var isPresented: Bool
    let onNavigateToLogin: () -> Void
    
    @Environment(\.outgoColors) private var colors
    @Environment(\.outgoTypography) private var typo
    @Environment(\.spacing) private var spacing
    
    func body(content: Content) -> some View {
        ZStack {
            content
            
            if isPresented {
                // --- Fond ---
                Color.black.opacity(0.4)
                    .ignoresSafeArea()
                    .onTapGesture { isPresented = false }
                    .transition(.opacity)
                
                // --- La Modale  ---
                VStack(spacing: spacing.medium) {
                    Image("cloud_check")
                        .resizable()
                        .renderingMode(.template)
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 32, height: 32)
                        .foregroundColor(colors.primary)
                    
                    // Titre
                    Text(HeaderLabels.shared.SYNC_PROMO_TITLE)
                        .font(typo.subtitle)
                        .foregroundColor(colors.textPrimary)
                        .multilineTextAlignment(.center)
                    
                    // Description
                    Text(HeaderLabels.shared.SYNC_PROMO_DESC)
                        .font(typo.body)
                        .foregroundColor(colors.textSecondary)
                        .multilineTextAlignment(.center)
                        .fixedSize(horizontal: false, vertical: true)
                    
                    Spacer().frame(height: spacing.small)
                    
                    // Boutons d'actions
                    HStack(spacing: spacing.medium) {
                        // Action "Plus tard" (Gris)
                        Button(action: { isPresented = false }) {
                            Text(HeaderLabels.shared.SYNC_PROMO_ACTION_LATER)
                                .font(typo.body)
                                .fontWeight(.medium)
                                .foregroundColor(colors.textSecondary)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 12)
                        }
                        
                        // Action "Se connecter" (Primary)
                        Button(action: {
                            isPresented = false
                            onNavigateToLogin()
                        }) {
                            Text(HeaderLabels.shared.SYNC_PROMO_ACTION_LOGIN)
                                .font(typo.body)
                                .fontWeight(.bold)
                                .foregroundColor(colors.primary)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 12)
                        }
                    }
                }
                .padding(spacing.extraLarge)
                .background(colors.surface200)
                .cornerRadius(24)
                .padding(spacing.extraLarge)
                .transition(.scale(scale: 0.9).combined(with: .opacity))
            }
        }
        .animation(.spring(response: 0.3, dampingFraction: 0.7), value: isPresented)
    }
}

extension View {
    func syncPromotionModal(isPresented: Binding<Bool>, onNavigateToLogin: @escaping () -> Void) -> some View {
        self.modifier(SyncPromotionModal(isPresented: isPresented, onNavigateToLogin: onNavigateToLogin))
    }
}
