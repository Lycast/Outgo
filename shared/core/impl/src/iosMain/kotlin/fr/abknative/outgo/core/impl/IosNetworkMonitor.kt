package fr.abknative.outgo.core.impl

import fr.abknative.outgo.core.api.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Network.*
import platform.darwin.dispatch_get_main_queue

internal class IosNetworkMonitor : NetworkMonitor {

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val pathMonitor = nw_path_monitor_create()

    init {
        nw_path_monitor_set_update_handler(pathMonitor) { path ->
            val status = nw_path_get_status(path)
            _isConnected.value = (status == nw_path_status_satisfied)
        }

        nw_path_monitor_set_queue(pathMonitor, dispatch_get_main_queue())
        nw_path_monitor_start(pathMonitor)
    }
}