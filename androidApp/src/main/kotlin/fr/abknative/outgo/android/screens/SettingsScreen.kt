package fr.abknative.outgo.android.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.components.common.ConfirmationDialog
import fr.abknative.outgo.android.components.common.Header
import fr.abknative.outgo.android.components.common.SyncPromotionModal
import fr.abknative.outgo.android.components.settings.DeleteAccountDialog
import fr.abknative.outgo.android.components.settings.SettingsRowClickable
import fr.abknative.outgo.android.components.settings.SettingsRowToggle
import fr.abknative.outgo.android.components.settings.SettingsSection
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.DialogLabels
import fr.abknative.outgo.android.ui.SettingsLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.settings.api.SettingsIntent
import fr.abknative.outgo.settings.api.SettingsPresenter

/**
 * Settings screen allowing the user to manage app appearance, local data, and account synchronization.
 */
@Composable
fun SettingsScreen(
    presenter: SettingsPresenter,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by presenter.state.collectAsState()
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    // --- Navigation Effect ---
    LaunchedEffect(state.actionSuccess) {
        if (state.actionSuccess) {
            presenter.onIntent(SettingsIntent.ResetSuccessFlag)
        }
    }

    // --- Dialog States ---
    var showLogoutConfirm by remember { mutableStateOf(false) }
    var showDeleteAccountConfirm by remember { mutableStateOf(false) }
    var showPurgeConfirm by remember { mutableStateOf(false) }
    var showSyncModal by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val headerContent = @Composable { isVerticalLayout: Boolean ->
        Header(
            syncState = state.syncState,
            isVertical = isVerticalLayout,
            isSettingsScreen = true,
            onSyncIconClick = {
                when {
                    state.syncState.isUnauthenticated -> showSyncModal = true
                    state.syncState.isPending || state.syncState.isError || state.syncState.isUpToDate -> {
                        presenter.onIntent(SettingsIntent.RefreshSync)
                    }
                }
            },
            onSyncNavigationClick = onNavigateBack,
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            if (!isLandscape) {
                headerContent(false)
            }
        }
    ) { paddingValues ->

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isLandscape) PaddingValues(0.dp) else paddingValues)
        ) {
            if (isLandscape) {
                headerContent(true)
                VerticalDivider(
                    thickness = 1.dp,
                    color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(AppTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.large)
            ) {

                // --- SECTION 1 : Apparence ---
                SettingsSection(title = SettingsLabels.SECTION_APPEARANCE) {
                    SettingsRowToggle(
                        icon = R.drawable.moon_duotone,
                        title = SettingsLabels.DARK_MODE_TITLE,
                        subtitle = SettingsLabels.DARK_MODE_SUBTITLE,
                        isChecked = isDarkMode,
                        onCheckedChange = onToggleDarkMode
                    )
                }

                // --- SECTION 2 : Soutien & Communauté ---
                SettingsSection(title = SettingsLabels.SECTION_SUPPORT) {
                    SettingsRowClickable(
                        icon = R.drawable.lightbulb_duotone,
                        title = SettingsLabels.TIPS_TITLE,
                        subtitle = SettingsLabels.TIPS_SUBTITLE,
                        onClick = { uriHandler.openUri(SettingsLabels.URL_SITE) }
                    )
                    HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                    SettingsRowClickable(
                        icon = R.drawable.envelope_duotone,
                        title = SettingsLabels.CONTACT_TITLE,
                        subtitle = SettingsLabels.CONTACT_SUBTITLE,
                        onClick = { uriHandler.openUri(SettingsLabels.URL_CONTACT) }
                    )
                }

                if (state.session == null) {
                    SettingsSection(title = SettingsLabels.SECTION_DATA_AND_ACCOUNT) {
                        SettingsRowClickable(
                            icon = R.drawable.arrows_clockwise_duotone,
                            title = SettingsLabels.SYNC_TITLE,
                            subtitle = SettingsLabels.SYNC_SUBTITLE,
                            onClick = { showSyncModal = true }
                        )
                        HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                        SettingsRowClickable(
                            icon = R.drawable.trash_duotone,
                            title = DialogLabels.PURGE_TITLE,
                            subtitle = SettingsLabels.PURGE_SUBTITLE,
                            onClick = { showPurgeConfirm = true }
                        )
                    }
                } else {
                    // UTILISATEUR CONNECTÉ
                    SettingsSection(title = SettingsLabels.SECTION_ACCOUNT) {
                        SettingsRowClickable(
                            icon = R.drawable.sign_out_duotone,
                            title = SettingsLabels.LOGOUT_TITLE,
                            subtitle = SettingsLabels.LOGOUT_SUBTITLE,
                            onClick = { showLogoutConfirm = true }
                        )
                        HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                        SettingsRowClickable(
                            icon = R.drawable.trash_duotone,
                            title = SettingsLabels.DELETE_ACCOUNT_TITLE,
                            subtitle = SettingsLabels.DELETE_ACCOUNT_SUBTITLE,
                            onClick = { showDeleteAccountConfirm = true }
                        )
                    }
                }

                // Version de l'app (Footer)
                Text(
                    text = SettingsLabels.APP_VERSION_PREFIX,
                    style = AppTheme.typo.caption,
                    color = AppTheme.colors.textPrimary.toColor(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.spacing.large),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // --- MODALES DE CONFIRMATION ---
    if (showLogoutConfirm) {
        ConfirmationDialog(
            title = DialogLabels.LOGOUT_TITLE,
            description = DialogLabels.LOGOUT_DESC,
            confirmLabel = DialogLabels.LOGOUT_CONFIRM,
            cancelLabel = CommonLabels.ACTION_CANCEL,
            isDestructive = true,
            onConfirm = {
                presenter.onIntent(SettingsIntent.Logout)
                showLogoutConfirm = false
            },
            onDismiss = { showLogoutConfirm = false }
        )
    }

    if (showDeleteAccountConfirm) {
        DeleteAccountDialog(
            onConfirm = { wipeLocal, wipeServer, revokeAuth ->
                presenter.onIntent(SettingsIntent.DeleteAccount(wipeLocal, wipeServer, revokeAuth))
                showDeleteAccountConfirm = false
            },
            onDismiss = { showDeleteAccountConfirm = false }
        )
    }

    if (showPurgeConfirm) {
        ConfirmationDialog(
            title = DialogLabels.PURGE_TITLE,
            description = DialogLabels.PURGE_DESC,
            confirmLabel = DialogLabels.PURGE_CONFIRM,
            cancelLabel = CommonLabels.ACTION_CANCEL,
            isDestructive = true,
            onConfirm = {
                presenter.onIntent(SettingsIntent.PurgeLocalData)
                showPurgeConfirm = false
            },
            onDismiss = { showPurgeConfirm = false }
        )
    }

    if (showSyncModal) {
        SyncPromotionModal(
            onDismiss = { showSyncModal = false },
            onNavigateToLogin = {
                showSyncModal = false
                onNavigateToLogin()
            }
        )
    }
}