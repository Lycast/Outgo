package fr.abknative.outgo.android

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.screens.DashboardScreen
import fr.abknative.outgo.outgoing.api.presenter.OutgoingPresenter
import org.koin.androidx.compose.koinViewModel

@Composable
fun App() {

    val presenter: OutgoingPresenter = koinViewModel()

    MaterialTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            DashboardScreen(presenter = presenter)
        }
    }
}