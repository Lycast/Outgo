import SwiftUI
import SharedApp

struct ExpenseListContainer: View {
    let isLoading: Bool
    let filteredList: [Outgoing]
    let currentFilter: OutgoingFilter
    let onDelete: (String) -> Void
    let onEdit: (Outgoing) -> Void
    
    @Environment(\.outgoColors) private var colors
    @Environment(\.spacing) private var spacing
    
    var body: some View {
        VStack(spacing: 0) {
            ScrollView {
                LazyVStack(spacing: 0) {
                    if isLoading {
                        LoaderItem()
                    } else if filteredList.isEmpty {
                        EmptyStateView(filter: currentFilter)
                            .id("empty_state")
                    } else {
                        ForEach(filteredList, id: \.id) { (outgoing: Outgoing) in
                            OutgoingCard(
                                outgoing: outgoing,
                                onEdit: { onEdit(outgoing) },
                                onDelete: { onDelete(outgoing.id) },
                                onDuplicate: {
                                    let duplicated = outgoing.doCopy(
                                        id: "",
                                        budgetId: outgoing.budgetId,
                                        name: outgoing.name,
                                        amountInCents: outgoing.amountInCents,
                                        recurrence: outgoing.recurrence,
                                        dueDay: outgoing.dueDay,
                                        dueMonth: outgoing.dueMonth,
                                        createdAt: outgoing.createdAt,
                                        updatedAt: outgoing.updatedAt,
                                        isDeleted: outgoing.isDeleted,
                                        syncStatus: outgoing.syncStatus
                                    )
                                    onEdit(duplicated)
                                }
                            )
                            
                            Divider()
                                .background(colors.onSurfaceVariant.opacity(0.2))
                                .padding(.horizontal, spacing.large)
                        }
                    }
                }
                .padding(.bottom, 80)
            }
        }
        .frame(maxWidth: .infinity)
        .background(colors.surface)
        .clipShape(
            UnevenRoundedRectangle(
                topLeadingRadius: 12,
                bottomLeadingRadius: 0,
                bottomTrailingRadius: 0,
                topTrailingRadius: 12
            )
        )
        .overlay(
            UnevenRoundedRectangle(
                topLeadingRadius: 12,
                bottomLeadingRadius: 0,
                bottomTrailingRadius: 0,
                topTrailingRadius: 12
            )
            .stroke(colors.onSurfaceVariant.opacity(0.2), lineWidth: 1)
        )
        .padding(.horizontal, spacing.medium)
    }
}
