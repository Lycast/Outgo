import SwiftUI
import SharedApp

struct DashboardScreen: View {
    let presenter: OutgoingPresenter
    let onNavigateToSettings: () -> Void
    
    @State private var state: OutgoingState = OutgoingState(
        isLoading: true,
        outgoings: [],
        currentDay: nil,
        currentMonth: 1,
        selectedMonth: 1,
        monthlyIncomeInCents: 0,
        totalOutgoingsInCents: 0,
        disposableIncomeInCents: 0,
        remainingToPayInCents: 0,
        isCloudSyncActive: false,
        error: nil,
        isHeroExpanded: true
    )
    
    @State private var showBudgetModal = false
    @State private var showSyncModal = false
    @State private var showFormSheet = false
    @State private var selectedOutgoing: Outgoing? = nil
    @State private var currentFilter: OutgoingFilter = .all
    
    @Environment(\.spacing) private var spacing
    @Environment(\.outgoColors) private var colors

    private var filteredList: [Outgoing] {
        let currentDay = Int(truncating: state.currentDay ?? 0)
        let currentMonth = Int(state.currentMonth)
        let selectedMonth = Int(state.selectedMonth)
        
        switch currentFilter {
        case .all:
            return state.outgoings
        case .paid:
            if selectedMonth < currentMonth { return state.outgoings }
            if selectedMonth > currentMonth { return [] }
            return state.outgoings.filter { Int($0.dueDay) < currentDay }
        case .remaining:
            if selectedMonth < currentMonth { return [] }
            if selectedMonth > currentMonth { return state.outgoings }
            return state.outgoings.filter { Int($0.dueDay) >= currentDay }
        }
    }

    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            colors.background.ignoresSafeArea()
            
            VStack(spacing: 0) {
                Header(
                    isConnected: state.isCloudSyncActive,
                    isSettingsScreen: false,
                    onSyncIconClick: { showSyncModal = true },
                    onSyncNavigationClick: onNavigateToSettings
                )
            
                VStack(spacing: 0) {
                    HeroSection(
                        isExpanded: state.isHeroExpanded,
                        formattedMonthDate: getMonthName(month: state.selectedMonth),
                        monthlyIncomeInCents: state.monthlyIncomeInCents,
                        totalOutgoingsInCents: state.totalOutgoingsInCents,
                        disposableIncomeInCents: state.disposableIncomeInCents,
                        remainingToPayInCents: state.remainingToPayInCents,
                        onToggleExpand: {
                            presenter.onIntent(intent: OutgoingIntentToggleHeroSection(isExpanded: !state.isHeroExpanded))
                        },
                        onEditBudgetClick: { showBudgetModal = true },
                        onPreviousMonthClick: {
                            let newMonth = state.selectedMonth == 1 ? 12 : state.selectedMonth - 1
                            presenter.onIntent(intent: OutgoingIntentSelectMonth(month: newMonth))
                        },
                        onNextMonthClick: {
                            let newMonth = state.selectedMonth == 12 ? 1 : state.selectedMonth + 1
                            presenter.onIntent(intent: OutgoingIntentSelectMonth(month: newMonth))
                        }
                    )
                    Spacer().frame(height: spacing.extraLarge)
                    
                    ExpenseFilterSelector(
                        selectedFilter: currentFilter,
                        onFilterSelected: { currentFilter = $0 }
                    )
                    
                    Spacer().frame(height: spacing.medium)
                    
                    ExpenseListContainer(
                        isLoading: state.isLoading,
                        filteredList: filteredList,
                        currentFilter: currentFilter,
                        onDelete: { id in presenter.onIntent(intent: OutgoingIntentDelete(id: id)) },
                        onEdit: { outgoing in
                            selectedOutgoing = outgoing
                            showFormSheet = true
                        }
                    )
                }
                .animation(.spring(response: 0.4, dampingFraction: 1), value: state.isHeroExpanded)
            }
            
            AddActionTrigger(onClick: {
                selectedOutgoing = nil
                showFormSheet = true
            })
            .padding(spacing.large)
        }
        .background(colors.background.ignoresSafeArea())
        .syncPromotionModal(isPresented: $showSyncModal) { }
        .budgetEditModal(
            isPresented: $showBudgetModal,
            currentIncomeInCents: state.monthlyIncomeInCents
        ) { newAmount in
            presenter.onIntent(intent: OutgoingIntentUpdateIncome(amountInCents: newAmount))
        }
        .sheet(isPresented: $showFormSheet) {
            OutgoingFormSheet(
                state: OutgoingFormState(
                    outgoingId: selectedOutgoing?.id,
                    initialName: selectedOutgoing?.name ?? "",
                    initialAmount: selectedOutgoing != nil ? String(format: "%.2f", Double(selectedOutgoing!.amountInCents) / 100.0) : "",
                    initialRecurrence: selectedOutgoing?.recurrence ?? .monthly,
                    initialDueDay: selectedOutgoing != nil ? String(selectedOutgoing!.dueDay) : "",
                    initialDueMonth: selectedOutgoing?.dueMonth?.stringValue ?? ""
                ),
                onDismiss: { showFormSheet = false },
                onSave: { intent in
                    presenter.onIntent(intent: intent)
                }
            )
            .presentationDetents([.fraction(0.7), .large])
        }
        .task {
            for await currentState in presenter.state {
                self.state = currentState
                if !currentState.isLoading && currentState.monthlyIncomeInCents <= 0 {
                    showBudgetModal = true
                }
            }
        }
    }
    
    private func getMonthName(month: Int32) -> String {
        let index = Int(month) - 1
        let symbols = Calendar.current.monthSymbols
        return (index >= 0 && index < symbols.count) ? symbols[index] : ""
    }
}
