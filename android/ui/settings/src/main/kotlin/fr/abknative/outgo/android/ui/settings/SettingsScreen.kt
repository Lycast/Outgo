package fr.abknative.outgo.android.ui.settings

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.core.R
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.core.toCommonUIString
import fr.abknative.outgo.android.ui.settings.components.SettingsRowClickable
import fr.abknative.outgo.android.ui.settings.components.SettingsRowToggle
import fr.abknative.outgo.android.ui.settings.components.SettingsSection
import fr.abknative.outgo.settings.api.SettingsIntent
import fr.abknative.outgo.settings.api.SettingsPresenter

@Composable
fun SettingsScreen(
    presenter: SettingsPresenter,
    onError: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToDeleteAccount: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val state by presenter.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    val context = LocalContext.current
    val appVersion = remember(context) {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            "${packageInfo.versionName}"
        } catch (e: PackageManager.NameNotFoundException) { }
    }

    var showLogoutOptions by remember { mutableStateOf(false) }
    var showDeleteAccountConfirm by remember { mutableStateOf(false) }
    var showPurgeConfirm by remember { mutableStateOf(false) }

    val currentError = state.error
    val errorMessage = state.error?.toCommonUIString()

    val userEmail = state.session?.email
    val logoutSubtitle = if (!userEmail.isNullOrBlank()) { userEmail } else {
        SettingsLabels.LOGOUT_SUBTITLE
    }

    LaunchedEffect(state.requireAccountDeletionLogin) {
        if (state.requireAccountDeletionLogin) {
            presenter.onIntent(SettingsIntent.ResetSuccessFlag)
            onNavigateToDeleteAccount()
        }
    }

    LaunchedEffect(currentError) {
        if (currentError != null && errorMessage != null) {
            onError(errorMessage)
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
                    title = SettingsLabels.INFO_OUTGO_TITLE,
                    subtitle = SettingsLabels.INFO_OUTGO_SUBTITLE,
                    onClick = { uriHandler.openUri(SettingsLabels.URL_OUTGO) }
                )
                HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                SettingsRowClickable(
                    icon = R.drawable.star_duotone,
                    title = SettingsLabels.RATING_TITLE,
                    subtitle = SettingsLabels.RATING_SUBTITLE,
                    onClick = { uriHandler.openUri(SettingsLabels.URL_STORE) }
                )
                HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                SettingsRowClickable(
                    icon = R.drawable.envelope_duotone,
                    title = SettingsLabels.CONTACT_TITLE,
                    subtitle = SettingsLabels.CONTACT_SUBTITLE,
                    onClick = { uriHandler.openUri(SettingsLabels.URL_CONTACT) }
                )
            }

            // --- SECTION Data & Account ---
            if (state.session == null) {
                SettingsSection(title = SettingsLabels.SECTION_DATA_AND_ACCOUNT) {
                    SettingsRowClickable(
                        icon = R.drawable.arrows_clockwise_duotone,
                        title = SettingsLabels.SYNC_TITLE,
                        subtitle = SettingsLabels.SYNC_SUBTITLE,
                        onClick = { onNavigateToLogin() }
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
                text = "${SettingsLabels.APP_VERSION_PREFIX}$appVersion",
                style = AppTheme.typo.caption,
                color = AppTheme.colors.textPrimary.toColor(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimens.large),
                textAlign = TextAlign.Center
            )
        }
        SettingsModals(
            showLogoutOptions = showLogoutOptions,
            showDeleteAccountDialog = showDeleteAccountConfirm,
            showLocalPurgeDialog = showPurgeConfirm,
            onDismissLogout = { showLogoutOptions = false },
            onDismissDeleteAccount = { showDeleteAccountConfirm = false },
            onDismissLocalPurge = { showPurgeConfirm = false },
            presenter = presenter
        )
    }
}