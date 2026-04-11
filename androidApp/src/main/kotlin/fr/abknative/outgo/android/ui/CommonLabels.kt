package fr.abknative.outgo.android.ui

import androidx.compose.runtime.Composable
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

object CommonLabels {

    /*

    <string name="app_name">Outgo</string>
    <string name="common_action_save">Enregistrer</string>
    <string name="common_action_cancel">Annuler</string>
    <string name="common_action_delete">Supprimer</string>
    <string name="common_action_edit">Modifier</string>
    <string name="common_action_duplicate">Dupliquer</string>
    <string name="common_action_close">Fermer</string>

     */

    val APP_NAME @Composable get() = stringResource(Res.string.app_name)
    const val CURRENCY_SYMBOL = "€"

    val ACTION_SAVE @Composable get() = stringResource(Res.string.common_action_save)
    val ACTION_CANCEL @Composable get() = stringResource(Res.string.common_action_cancel)
    val ACTION_DELETE @Composable get() = stringResource(Res.string.common_action_delete)
    val ACTION_EDIT @Composable get() = stringResource(Res.string.common_action_edit)
    val ACTION_DUPLICATE @Composable get() = stringResource(Res.string.common_action_duplicate)
    val ACTION_CLOSE @Composable get() = stringResource(Res.string.common_action_close)

    // --- Temporaire ---
    const val ACTION_OK = "OK"
    const val SYNC_OFFLINE_TITLE = "Réseau indisponible"
    const val SYNC_OFFLINE_DESC = "Vérifiez votre connexion internet. L'application fonctionne normalement hors-ligne, la synchronisation reprendra plus tard."
}

object DialogLabels {

    /*

    <string name="dialog_delete_operation_title">Supprimer l'opération</string>
    <string name="dialog_delete_operation_desc">Êtes-vous sûr de vouloir supprimer définitivement cette dépense ? Cette action est irréversible.</string>
    <string name="dialog_logout_title">Se déconnecter ?</string>
    <string name="dialog_logout_desc">Vous devrez vous reconnecter pour synchroniser vos prochaines modifications avec le cloud.</string>
    <string name="dialog_logout_confirm">Déconnexion</string>
    <string name="dialog_purge_title">Vider les données locales</string>
    <string name="dialog_purge_desc">Cela supprimera toutes les données Outgo présentes sur cet appareil.</string>
    <string name="dialog_purge_confirm">Vider le cache</string>
    <string name="dialog_delete_account_title">Supprimer vos données</string>
    <string name="dialog_delete_account_desc">Cette action est définitive. Toutes vos données seront effacées de nos serveurs de manière irréversible.</string>

    <string name="budget_dialog_title">Mon budget mensuel</string>
    <string name="budget_dialog_desc">Entrez votre budget total par mois.</string>
    <string name="budget_dialog_info">Le montant de votre budget permettra ensuite de calculer ce qu'il vous restera en fonction de vos dépenses.</string>
    <string name="budget_dialog_field">Montant du budget</string>

     */

    // --- Opération : Suppression ---
    val DELETE_OPERATION_TITLE @Composable get() = stringResource(Res.string.dialog_delete_operation_title)
    val DELETE_OPERATION_DESC @Composable get() = stringResource(Res.string.dialog_delete_operation_desc)

    // --- Paramètres : Déconnexion ---
    val LOGOUT_TITLE @Composable get() = stringResource(Res.string.dialog_logout_title)
    val LOGOUT_DESC @Composable get() = stringResource(Res.string.dialog_logout_desc)
    val LOGOUT_CONFIRM @Composable get() = stringResource(Res.string.dialog_logout_confirm)

    // --- Paramètres : Purge Cache Local ---
    val PURGE_TITLE @Composable get() = stringResource(Res.string.dialog_purge_title)
    val PURGE_DESC @Composable get() = stringResource(Res.string.dialog_purge_desc)
    val PURGE_CONFIRM @Composable get() = stringResource(Res.string.dialog_purge_confirm)

    // --- Paramètres : Suppression Compte ---
    val DELETE_ACCOUNT_TITLE @Composable get() = stringResource(Res.string.dialog_delete_account_title)
    val DELETE_ACCOUNT_DESC @Composable get() = stringResource(Res.string.dialog_delete_account_desc)
    val DIALOG_BUDGET_TITLE @Composable get() = stringResource(Res.string.budget_dialog_title)
    val DIALOG_BUDGET_DESC @Composable get() = stringResource(Res.string.budget_dialog_desc)
    val DIALOG_BUDGET_INFO @Composable get() = stringResource(Res.string.budget_dialog_info)
    val DIALOG_BUDGET_FIELD @Composable get() = stringResource(Res.string.budget_dialog_field)


    // --- Temporaire ---
    const val DELETE_ACCOUNT_CHOICE_DESC = "Choisissez ce que vous souhaitez effacer. Cette action est irréversible."

    const val DELETE_ACCOUNT_LOCAL_TITLE = "Vider cet appareil"
    const val DELETE_ACCOUNT_LOCAL_DESC = "Supprime vos données de cet appareil."

    const val DELETE_ACCOUNT_SERVER_TITLE = "Effacer mon Cloud"
    const val DELETE_ACCOUNT_SERVER_DESC = "Supprime vos données des serveurs Outgo."

    const val DELETE_ACCOUNT_AUTH_TITLE = "Supprimer mon profil"
    const val DELETE_ACCOUNT_AUTH_DESC = "Révoque l'accès et détruit vos données personnelles."
    const val LOGOUT_DATA_QUESTION = "Que souhaitez-vous faire de vos données actuelles ?"
    const val LOGOUT_ACTION_KEEP_BUDGET = "Conserver ce budget"
    const val LOGOUT_ACTION_RETURN_LOCAL = "Retrouver mon budget local"
}

object FormLabels {

    /*

     <string name="form_sheet_title_add">Nouvelle opération</string>
    <string name="form_sheet_title_edit">Modifier l'opération</string>
    <string name="form_field_name">Entrez l'intitulé</string>
    <string name="form_field_place_holder_name">Ex: Loyer, Netflix...</string>
    <string name="form_field_amount">Entrez le montant</string>
    <string name="form_field_place_holder_amount">0.00</string>
    <string name="form_field_date_desc">Sélectionnez la récurrence et entrez la date de votre dépense.</string>
    <string name="form_cycle_unique">Unique</string>
    <string name="form_cycle_weekly">Hebdo</string>
    <string name="form_cycle_monthly">Mensuel</string>
    <string name="form_cycle_yearly">Annuel</string>

     */

    val SHEET_TITLE_ADD @Composable get() = stringResource(Res.string.form_sheet_title_add)
    val SHEET_TITLE_EDIT @Composable get() = stringResource(Res.string.form_sheet_title_edit)
    val FIELD_NAME @Composable get() = stringResource(Res.string.form_field_name)
    val FIELD_PLACE_HOLDER_NAME @Composable get() = stringResource(Res.string.form_field_place_holder_name)
    val FIELD_AMOUNT @Composable get() = stringResource(Res.string.form_field_amount)
    val FIELD_PLACE_HOLDER_AMOUNT @Composable get() = stringResource(Res.string.form_field_place_holder_amount)
    val FIELD_DATE_DESC @Composable get() = stringResource(Res.string.form_field_date_desc)
    val CYCLE_UNIQUE @Composable get() = stringResource(Res.string.form_cycle_unique)
    val CYCLE_WEEKLY @Composable get() = stringResource(Res.string.form_cycle_weekly)
    val CYCLE_MONTHLY @Composable get() = stringResource(Res.string.form_cycle_monthly)
    val CYCLE_YEARLY @Composable get() = stringResource(Res.string.form_cycle_yearly)

    // --- Temporaire ---
    const val FIELD_RECURRENCE_DESC = "Sélectionnez la récurrence"
    const val FIELD_TYPE_DESC = "Sélectionnez le type d'opération"
    const val TYPE_EXPENSE = "Dépense"
    const val TYPE_INCOME = "Revenu"
    const val FIELD_DATE_LABEL = "Date de l'opération"
}

object AccessibilityLabels {

    /*

    <string name="a11y_loading">Chargement en cours</string>
    <string name="a11y_sync_error">Synchronisation échouée</string>
    <string name="a11y_synced">Synchronisé avec le serveur</string>
    <string name="a11y_not_synced">Non synchronisé, appuyez pour configurer</string>
    <string name="a11y_delete_expense">Supprimer cette dépense</string>
    <string name="a11y_edit_expense">Modifier cette dépense</string>
    <string name="a11y_duplicate_expense">Dupliquer cette dépense</string>
    <string name="a11y_edit_budget">Modifier le budget</string>
    <string name="a11y_navigate_home">Retour au Dashboard</string>
    <string name="a11y_navigate_settings">Paramètres</string>
    <string name="a11y_previous_month">Mois précédent</string>
    <string name="a11y_next_month">Mois suivant</string>
    <string name="a11y_expand_hero">Développer les détails</string>
    <string name="a11y_collapse_hero">Réduire les détails</string>
    <string name="a11y_expand_hero_desc">Déplié</string>
    <string name="a11y_collapse_hero_desc">Replié</string>
    <string name="a11y_add_expense">Ajouter une nouvelle dépense</string>
    <string name="a11y_info_tooltip">Afficher les détails</string>
    <string name="a11y_info_empty_state">Information : liste vide</string>
    <string name="a11y_day_selector">Selection de jour</string>
    <string name="a11y_month_selector">Selection de mois</string>

     */

    val LOADING @Composable get() = stringResource(Res.string.a11y_loading)
    val SYNCED @Composable get() = stringResource(Res.string.a11y_synced)
    val NOT_SYNCED @Composable get() = stringResource(Res.string.a11y_not_synced)
    val DELETE_EXPENSE @Composable get() = stringResource(Res.string.a11y_delete_expense)
    val EDIT_EXPENSE @Composable get() = stringResource(Res.string.a11y_edit_expense)
    val DUPLICATE_EXPENSE @Composable get() = stringResource(Res.string.a11y_duplicate_expense)
    val EDIT_BUDGET @Composable get() = stringResource(Res.string.a11y_edit_budget)
    val NAVIGATE_HOME @Composable get() = stringResource(Res.string.a11y_navigate_home)
    val NAVIGATE_SETTINGS @Composable get() = stringResource(Res.string.a11y_navigate_settings)
    val PREVIOUS_MONTH @Composable get() = stringResource(Res.string.a11y_previous_month)
    val NEXT_MONTH @Composable get() = stringResource(Res.string.a11y_next_month)
    val EXPAND_HERO @Composable get() = stringResource(Res.string.a11y_expand_hero)
    val COLLAPSE_HERO @Composable get() = stringResource(Res.string.a11y_collapse_hero)
    val EXPAND_DESC @Composable get() = stringResource(Res.string.a11y_expand_hero_desc)
    val COLLAPSE_DESC @Composable get() = stringResource(Res.string.a11y_collapse_hero_desc)
    val ADD_EXPENSE @Composable get() = stringResource(Res.string.a11y_add_expense)
    val INFO_TOOLTIP @Composable get() = stringResource(Res.string.a11y_info_tooltip)
    val INFO_EMPTY_STATE @Composable get() = stringResource(Res.string.a11y_info_empty_state)
    val DAY_SELECTOR @Composable get() = stringResource(Res.string.a11y_day_selector)
    val MONTH_SELECTOR @Composable get() = stringResource(Res.string.a11y_month_selector)
    val SYNC_ERROR @Composable get() = stringResource(Res.string.a11y_sync_error)

    // --- Temporaire ---
    const val NAVIGATE_ANALYSE = "Analyse"
    const val PREMIUM_BADGE = "Premium"
    const val NEXT_VIEW = "Vue suivante"
    const val PREVIOUS_VIEW = "Vue précédente"
}
