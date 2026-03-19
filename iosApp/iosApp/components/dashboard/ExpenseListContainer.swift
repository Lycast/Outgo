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
        ScrollView(showsIndicators: false) {
            LazyVStack(spacing: spacing.medium) {
                
                if isLoading {
                    LoaderItem()
                } else if filteredList.isEmpty {
                    EmptyStateView(filter: currentFilter)
                        .id("empty_state")
                } else {
                    ForEach(filteredList, id: \.id) { outgoing in
                        
                        VStack(spacing: 0) {
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
                        }
                        .background(colors.surface50)
                        .cornerRadius(12)
                        .overlay(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(colors.textSecondary.opacity(0.1), lineWidth: 1)
                        )
                        .padding(.horizontal, spacing.medium)
                    }
                }
            }
            .padding(.bottom, 100)
        }
    }
}
