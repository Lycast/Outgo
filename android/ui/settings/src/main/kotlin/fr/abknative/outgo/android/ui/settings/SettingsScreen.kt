package fr.abknative.outgo.android.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.core.R
import fr.abknative.outgo.android.core.SettingsLabels
import fr.abknative.outgo.android.core.components.feedback.AppSnackbar
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.core.toUIString
import fr.abknative.outgo.android.ui.settings.components.SettingsRowClickable
import fr.abknative.outgo.android.ui.settings.components.SettingsRowToggle
import fr.abknative.outgo.android.ui.settings.components.SettingsSection
import fr.abknative.outgo.settings.api.SettingsIntent
import fr.abknative.outgo.settings.api.SettingsPresenter

@Composable
fun SettingsScreen(
    presenter: SettingsPresenter,
    onNavigateToLogin: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val state by presenter.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current
    val snackbarHostState = remember { SnackbarHostState() }
    var showLogoutOptions by remember { mutableStateOf(false) }
    var showDeleteAccountConfirm by remember { mutableStateOf(false) }
    var showPurgeConfirm by remember { mutableStateOf(false) }
    val errorMessage = state.error?.toUIString()
    val siteUrl = SettingsLabels.URL_SITE
    val contactUrl = SettingsLabels.URL_CONTACT

    val userEmail = state.session?.email
    val logoutSubtitle = if (!userEmail.isNullOrBlank()) { userEmail } else {
        SettingsLabels.LOGOUT_SUBTITLE
    }

    LaunchedEffect(state.error) {
        if (state.error != null && errorMessage != null) {
            snackbarHostState.showSnackbar(message = errorMessage, withDismissAction = true)
            presenter.onIntent(SettingsIntent.DismissError)
        }
    }

    LaunchedEffect(state.actionSuccess) {
        if (state.actionSuccess) {
            presenter.onIntent(SettingsIntent.ResetSuccessFlag)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(AppTheme.dimens.medium),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.large)
        ) {

            // --- SECTION Apparence ---
            SettingsSection(title = SettingsLabels.SECTION_APPEARANCE) {
                SettingsRowToggle(
                    icon = R.drawable.moon_duotone,
                    title = SettingsLabels.DARK_MODE_TITLE,
                    subtitle = SettingsLabels.DARK_MODE_SUBTITLE,
                    isChecked = isDarkMode,
                    onCheckedChange = onToggleDarkMode
                )
            }

            // --- SECTION helpers & Community ---
            SettingsSection(title = SettingsLabels.SECTION_SUPPORT) {
                SettingsRowClickable(
                    icon = R.drawable.lightbulb_duotone,
                    title = SettingsLabels.TIPS_TITLE,
                    subtitle = SettingsLabels.TIPS_SUBTITLE,
                    onClick = { uriHandler.openUri(siteUrl) }
                )
                HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                SettingsRowClickable(
                    icon = R.drawable.envelope_duotone,
                    title = SettingsLabels.CONTACT_TITLE,
                    subtitle = SettingsLabels.CONTACT_SUBTITLE,
                    onClick = { uriHandler.openUri(contactUrl) }
                )
            }

            // --- SECTION Data & Account ---
            if (state.session == null) {
                SettingsSection(title = SettingsLabels.SECTION_DATA_AND_ACCOUNT) {
                    SettingsRowClickable(
                        icon = R.drawable.arrows_clockwise_duotone,
                        title = SettingsLabels.SYNC_TITLE,
                        subtitle = SettingsLabels.SYNC_SUBTITLE,
                        onClick = onNavigateToLogin
                    )
                    HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                    SettingsRowClickable(
                        icon = R.drawable.trash_duotone,
                        title = SettingsLabels.PURGE_TITLE,
                        subtitle = SettingsLabels.PURGE_SUBTITLE,
                        onClick = { showPurgeConfirm = true }
                    )
                }
            } else {
                SettingsSection(title = SettingsLabels.SECTION_ACCOUNT) {
                    SettingsRowClickable(
                        icon = R.drawable.sign_out_duotone,
                        title = SettingsLabels.LOGOUT_TITLE,
                        subtitle = logoutSubtitle,
                        onClick = { showLogoutOptions = true }
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

            // Footer
            Text(
                text = SettingsLabels.APP_VERSION_PREFIX,
                style = AppTheme.typo.caption,
                color = AppTheme.colors.textPrimary.toColor(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimens.large),
                textAlign = TextAlign.Center
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        ) { data -> AppSnackbar(data) }

        SettingsModals(
            showLogoutOptions = showLogoutOptions,
            showDeleteAccountConfirm = showDeleteAccountConfirm,
            showPurgeConfirm = showPurgeConfirm,
            onDismissLogout = { showLogoutOptions = false },
            onDismissDelete = { showDeleteAccountConfirm = false },
            onDismissPurge = { showPurgeConfirm = false },
            presenter = presenter
        )
    }
}