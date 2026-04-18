package fr.abknative.outgo.android.components.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.components.feedback.AppLoader
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OperationListContainer(
    isLoading: Boolean,
    groupedOperations: Map<String, List<ProjectedOperation>>,
    onDeleteRequest: (ProjectedOperation) -> Unit,
    onEdit: (ProjectedOperation) -> Unit,
    modifier: Modifier = Modifier
) {

    // 1. Gestion du chargement
    if (isLoading) {
        // Idéalement, centre ce loader dans une Box(Modifier.fillMaxSize())
        AppLoader()
        return
    }

    // 2. Gestion de l'état vide
    if (groupedOperations.isEmpty()) {
        // TODO: Affiche ton composant ListEmptyState.
        // Note: L'ancien prenait 'currentFilter' en paramètre. Il faudra l'adapter
        // pour qu'il affiche un message générique ("Rien ici !").
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        groupedOperations.forEach { (headerTitle, operations) ->

            if (headerTitle != "GLOBAL_RULES") {
                item(key = "header_$headerTitle") {
                    AppText(
                        text = headerTitle,
                        style = AppTheme.typo.label.copy(fontWeight = FontWeight.Bold),
                        color = AppTheme.colors.primary.toColor(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = AppTheme.dimens.medium)
                            .padding(top = AppTheme.dimens.large)
                    )
                }
            }

            items(
                items = operations,
                key = { "${it.operation.id}_${it.projectedDate}" }
            ) { projectedOp ->

                Spacer(modifier = Modifier.height(AppTheme.dimens.small))

                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val displayOperation = projectedOp.operation.copy(
                        startDate = projectedOp.projectedDate
                    )

                    OperationCard(
                        operation = displayOperation,
                        formattedDate = projectedOp.formattedDate,
                        onEdit = { onEdit(projectedOp) },
                        onDeleteRequest = { onDeleteRequest(projectedOp) }
                    )
                }
            }
        }
    }
}