package fr.abknative.outgo.android.ui.settings

import androidx.compose.runtime.Composable
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

internal object SettingsLabels {

    val SECTION_APPEARANCE @Composable get() = stringResource(Res.string.settings_section_appearance)
    val SECTION_SUPPORT @Composable get() = stringResource(Res.string.settings_section_support)
    val SECTION_DATA_AND_ACCOUNT @Composable get() = stringResource(Res.string.settings_section_data_account)
    val SECTION_ACCOUNT @Composable get() = stringResource(Res.string.settings_section_account)

    val DARK_MODE_TITLE @Composable get() = stringResource(Res.string.settings_dark_mode_title)
    val DARK_MODE_SUBTITLE @Composable get() = stringResource(Res.string.settings_dark_mode_subtitle)

    val INFO_OUTGO_TITLE @Composable get() = stringResource(Res.string.settings_info_outgo_title)
    val INFO_OUTGO_SUBTITLE @Composable get() = stringResource(Res.string.settings_info_outgo_subtitle)
    val RATING_TITLE @Composable get() = stringResource(Res.string.settings_rating_title)
    val RATING_SUBTITLE @Composable get() = stringResource(Res.string.settings_rating_subtitle)
    val CONTACT_TITLE @Composable get() = stringResource(Res.string.settings_contact_title)
    val CONTACT_SUBTITLE @Composable get() = stringResource(Res.string.settings_contact_subtitle)

    val SYNC_TITLE @Composable get() = stringResource(Res.string.settings_sync_title)
    val SYNC_SUBTITLE @Composable get() = stringResource(Res.string.settings_sync_subtitle)
    val LOGOUT_TITLE @Composable get() = stringResource(Res.string.settings_logout_title)
    val LOGOUT_SUBTITLE @Composable get() = stringResource(Res.string.settings_logout_subtitle)
    val DELETE_ACCOUNT_TITLE @Composable get() = stringResource(Res.string.settings_delete_account)
    val DELETE_ACCOUNT_SUBTITLE @Composable get() = stringResource(Res.string.settings_delete_account_subtitle)
    val PURGE_TITLE @Composable get() = stringResource(Res.string.settings_local_purge_title)
    val PURGE_SUBTITLE @Composable get() = stringResource(Res.string.settings_local_purge_subtitle)

    val PURGE_DESC @Composable get() = stringResource(Res.string.settings_dialog_purge_desc)
    val PURGE_CONFIRM @Composable get() = stringResource(Res.string.settings_dialog_purge_confirm)
    val DELETE_ACCOUNT_CHOICE_DESC @Composable get() = stringResource(Res.string.settings_dialog_delete_account_choice_desc)
    val DELETE_ACCOUNT_SERVER_TITLE @Composable get() = stringResource(Res.string.settings_dialog_delete_account_server_title)
    val DELETE_ACCOUNT_SERVER_DESC @Composable get() = stringResource(Res.string.settings_dialog_delete_account_server_desc)
    val DELETE_ACCOUNT_AUTH_TITLE @Composable get() = stringResource(Res.string.settings_dialog_delete_account_auth_title)
    val DELETE_ACCOUNT_AUTH_DESC @Composable get() = stringResource(Res.string.settings_dialog_delete_account_auth_desc)
    val LOGOUT_DATA_QUESTION @Composable get() = stringResource(Res.string.settings_dialog_logout_data_question)
    val LOGOUT_ACTION_KEEP_BUDGET @Composable get() = stringResource(Res.string.settings_dialog_logout_action_keep_budget)
    val LOGOUT_ACTION_RETURN_LOCAL @Composable get() = stringResource(Res.string.settings_dialog_logout_action_return_local)

    val APP_VERSION_PREFIX @Composable get() = stringResource(Res.string.settings_app_version_prefix)

    const val URL_OUTGO = "https://abknative.fr/outgo"
    const val URL_STORE = "https://play.google.com/store/apps/details?id=fr.abknative.outgo"
    const val URL_CONTACT = "https://abknative.fr/contact"
}