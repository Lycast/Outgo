package fr.abknative.outgo.android.outgoing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.toUIString
import fr.abknative.outgo.outgoing.api.BillingCycle
import fr.abknative.outgo.outgoing.api.presenter.OutgoingIntent
import fr.abknative.outgo.outgoing.api.presenter.OutgoingPresenter
import org.koin.androidx.compose.koinViewModel

@Composable
fun OutgoingScreen(
    presenter: OutgoingPresenter = koinViewModel()
) {
    // 1. Observation réactive de l'état
    val state by presenter.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // --- GESTION DES ERREURS ---
        state.error?.let { error ->
            Card(colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    // Utilisation du mapper sémantique -> UI créé précédemment
                    Text(text = error.toUIString(), color = Color.Red, modifier = Modifier.weight(1f))
                    Button(onClick = { presenter.onIntent(OutgoingIntent.DismissError) }) {
                        Text("OK")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- US 4 : LES CALCULS ---
        Text(text = "Tableau de bord", style = MaterialTheme.typography.titleLarge)
        Text(text = "Total lissé : ${state.monthlyTotalInCents / 100} €")
        Text(text = "Reste à payer : ${state.remainingToPayThisMonthInCents / 100} €")

        Spacer(modifier = Modifier.height(24.dp))

        // --- US 2 : AJOUT (Bouchonné pour le test) ---
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // Envoi d'une intention d'ajout avec des données statiques pour valider le flux
                presenter.onIntent(
                    OutgoingIntent.Add(
                        name = "Netflix",
                        amountInCents = 1399, // 13.99 €
                        cycle = BillingCycle.MONTHLY,
                        billingDay = 15
                    )
                )
            }
        ) {
            Text("Ajouter un abonnement test (13.99€, le 15)")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- US 1 & US 3 : LISTE ET SUPPRESSION ---
        Text(text = "Vos abonnements", style = MaterialTheme.typography.titleLarge)
        if (state.isLoading && state.outgoings.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.outgoings, key = { it.id }) { outgoing ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = outgoing.name, style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Prélevé le ${outgoing.billingDay}", style = MaterialTheme.typography.bodySmall)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "${outgoing.amountInCents / 100} €")
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { presenter.onIntent(OutgoingIntent.Delete(outgoing.id)) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("X")
                            }
                        }
                    }
                }
            }
        }
    }
}