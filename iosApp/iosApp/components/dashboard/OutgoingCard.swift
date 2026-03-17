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
    @Environment(\.spacing) private var spacing

    var body: some View {
        VStack(spacing: 0) {
            // --- PARTIE 1 : Contenu toujours visible ---
            HStack(alignment: .center, spacing: spacing.large) {
                
                CircleIcon(iconName: "credit_card_duotone")
                
                VStack(alignment: .leading, spacing: spacing.small) {
                    // Titre
                    Text(outgoing.uiTitle)
                        .font(.body)
                        .fontWeight(.semibold)
                        .foregroundColor(colors.onSurface)
                        .lineLimit(1)
                        .truncationMode(.tail)
                    
                    // Date, Récurrence et Montant
                    HStack(alignment: .bottom) {
                        HStack(spacing: 0) {
                            Text("\(outgoing.uiDueDayLabel) • ")
                                .font(.caption)
                                .foregroundColor(colors.onSurfaceVariant)
                            
                            Text(outgoing.uiFrequencySummary)
                                .font(.caption)
                                .foregroundColor(Color(outgoing.recurrence.uiRecurrenceColor).opacity(0.7))
                        }
                        
                        Spacer()
                        
                        Text(outgoing.amountInCents.uiAmount)
                            .font(.body)
                            .fontWeight(.semibold)
                            .foregroundColor(colors.tertiary)
                            .lineLimit(1)
                    }
                }
            }
            .padding(spacing.large)
            .contentShape(Rectangle()) // Rend toute la zone cliquable
            .onTapGesture {
                withAnimation(.spring(response: 0.4, dampingFraction: 0.8)) {
                    isExpanded.toggle()
                }
            }
            .onLongPressGesture {
                isExpanded = false
                onEdit()
            }

            // --- PARTIE 2 : Actions (visibles si déplié) ---
            if isExpanded {
                Divider()
                    .background(colors.onSurfaceVariant.opacity(0.05))
                    .padding(.horizontal, spacing.large)
                
                HStack(spacing: 0) {
                    // Bouton Supprimer
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
                    
                    // Bouton Dupliquer
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
                    
                    // Bouton Éditer
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
                .padding(.horizontal, spacing.extraLarge)
                .padding(.vertical, spacing.medium)
                .transition(.opacity.combined(with: .move(edge: .top)))
            }
        }
        .background(colors.surface)
    }

    // Helper pour créer les boutons d'action uniformes
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
                    .frame(width: 16, height: 16)
                    .foregroundColor(color)
                Text(title)
                    .font(.footnote)
            }
            .foregroundColor(color)
            .padding(.vertical, 8)
        }
        .accessibilityLabel(accessibility)
    }
}
