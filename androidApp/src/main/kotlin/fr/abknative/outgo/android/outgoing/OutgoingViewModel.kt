package fr.abknative.outgo.android.outgoing

import androidx.lifecycle.ViewModel
import fr.abknative.outgo.outgoing.api.presenter.OutgoingIntent
import fr.abknative.outgo.outgoing.api.presenter.OutgoingPresenter

class OutgoingViewModel(
    private val presenter: OutgoingPresenter
) : ViewModel() {

    val state = presenter.state

    fun onIntent(intent: OutgoingIntent) {
        presenter.onIntent(intent)
    }
}