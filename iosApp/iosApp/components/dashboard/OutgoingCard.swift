import SwiftUI
import SharedApp

struct OutgoingCard: View {
    // --- Propriétés ---
    let outgoing: Outgoing
    let onEdit: () -> Void
    let onDelete: () -> Void
    let onDuplicate: () -> Void
    
    // --- État Local ---
    @State private var isExpanded = false
    
    // --- Environnement ---
    @Environment(\.outgoColors) private var colors
    @Environment(\.outgoTypography) private var typo
    @Environment(\.spacing) private var spacing

    var body: some View {
        VStack(spacing: 0) {
            
            // --- Contenu toujours visible ---
            HStack(alignment: .center, spacing: spacing.large) {
                
                CircleIcon(
                    iconName: "credit_card_duotone",
                    tintColor: colors.primary,
                    containerColor: colors.surface100
                )
                
                VStack(alignment: .leading, spacing: 4) {
                    // Titre
                    Text(outgoing.uiTitle)
                        .font(typo.body)
                        .fontWeight(.semibold)
                        .foregroundColor(colors.textPrimary)
                        .lineLimit(1)
                    
                    // Date, Récurrence et Montant
                    HStack(alignment: .bottom) {
                        HStack(spacing: 0) {
                            Text("\(outgoing.uiDueDayLabel) • ")
                                .font(typo.caption)
                                .foregroundColor(colors.textSecondary)
                            
                            Text(outgoing.uiFrequencySummary)
                                .font(typo.caption)
                                .foregroundColor(outgoing.recurrence.uiRecurrenceColor.opacity(0.7))
                        }
                        
                        Spacer()
                        
                        // Montant (Poppins Body + Secondary)
                        Text(outgoing.amountInCents.uiAmount)
                            .font(typo.body)
                            .fontWeight(.semibold)
                            .foregroundColor(colors.secondary)
                            .lineLimit(1)
                    }
                }
            }
            .padding(spacing.large)
            .contentShape(Rectangle())
            .onTapGesture {
                withAnimation(.spring(response: 0.4, dampingFraction: 0.8)) {
                    isExpanded.toggle()
                }
            }
            .onLongPressGesture {
                // Vibration légère pour le feedback de clic long
                let impact = UIImpactFeedbackGenerator(style: .medium)
                impact.impactOccurred()
                
                isExpanded = false
                onEdit()
            }

            // --- Actions (visibles si déplié) ---
            if isExpanded {
                Divider()
                    .background(colors.textSecondary.opacity(0.05))
                    .padding(.horizontal, spacing.large)
                
                HStack(spacing: 0) {
                    // Bouton Supprimer (Error)
                    actionButton(
                        title: CommonLabels.shared.ACTION_DELETE,
                        icon: "trash",
                        color: colors.error,
                        accessibility: AccessibilityLabels.shared.DELETE_EXPENSE,
                        action: {
                            isExpanded = false
                            onDelete()
                        }
                    )
                    
                    Spacer()
                    
                    // Bouton Dupliquer (Primary)
                    actionButton(
                        title: CommonLabels.shared.ACTION_DUPLICATE,
                        icon: "copy",
                        color: colors.primary,
                        accessibility: AccessibilityLabels.shared.DUPLICATE_EXPENSE,
                        action: {
                            isExpanded = false
                            onDuplicate()
                        }
                    )
                    
                    Spacer()
                    
                    // Bouton Éditer (Primary)
                    actionButton(
                        title: CommonLabels.shared.ACTION_EDIT,
                        icon: "pencil_simple",
                        color: colors.primary,
                        accessibility: AccessibilityLabels.shared.EDIT_EXPENSE,
                        action: {
                            isExpanded = false
                            onEdit()
                        }
                    )
                }
                .padding(.horizontal, spacing.large)
                .padding(.vertical, spacing.extraSmall)
                .transition(.opacity.combined(with: .move(edge: .top)))
            }
        }
        .background(colors.surface50)
    }

    // Helper pour créer les boutons d'action
    @ViewBuilder
    private func actionButton(
        title: String,
        icon: String,
        color: Color,
        accessibility: String,
        action: @escaping () -> Void
    ) -> some View {
        Button(action: action) {
            HStack(spacing: spacing.extraSmall) {
                Image(icon)
                    .resizable()
                    .renderingMode(.template)
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 18, height: 18)
                
                Text(title)
                    .font(typo.caption)
                    .fontWeight(.medium)
            }
            .foregroundColor(color)
            .padding(.vertical, 12)
            .padding(.horizontal, 8)
        }
        .buttonStyle(.plain)
        .accessibilityLabel(accessibility)
    }
}
