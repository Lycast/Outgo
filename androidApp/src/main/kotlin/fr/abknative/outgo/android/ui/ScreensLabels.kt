package fr.abknative.outgo.android.ui

import androidx.compose.runtime.Composable
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

object OnboardingLabels {
    // --- Temporaire : Step Configuration ---
    const val CONFIG_TITLE = "Créons votre espace"
    const val WALLET_NAME_LABEL = "Nom de votre portefeuille"
    const val WALLET_NAME_PLACEHOLDER = "Ex: Mon Budget, Compte Courant..."
    const val INCOME_AMOUNT_LABEL = "Revenu mensuel principal (€)"
    const val INCOME_AMOUNT_PLACEHOLDER = "Ex: 2500"
    const val SUBMIT_ONBOARDING = "Générer mon tableau de bord"

    // --- Temporaire : Step Welcome ---
    const val WELCOME_TITLE = "Reprenez le contrôle\nde votre budget."
    const val WELCOME_SUBTITLE = "Sans friction. Sans connexion obligatoire. Vos données restent sur votre appareil."
    const val WELCOME_ACTION_START = "Commencer"
    const val WELCOME_ACTION_LOGIN = "Vous avez déjà un compte? Connectez vous"
}

object HeaderLabels {

    /*

    <string name="header_sync_promo_title">Persistance de vos données</string>
    <string name="header_sync_promo_desc">Vous pouvez synchroniser gratuitement vos données sur le serveur pour ne jamais les perdre et les rendre accessibles depuis n'importe quel téléphone.</string>
    <string name="header_sync_promo_action_login">Synchronisation</string>
    <string name="header_sync_promo_action_later">Plus tard</string>

     */

    val SYNC_PROMO_TITLE @Composable get() = stringResource(Res.string.header_sync_promo_title)
    val SYNC_PROMO_DESC @Composable get() = stringResource(Res.string.header_sync_promo_desc)
    val SYNC_PROMO_ACTION_LOGIN @Composable get() = stringResource(Res.string.header_sync_promo_action_login)
    val SYNC_PROMO_ACTION_LATER @Composable get() = stringResource(Res.string.header_sync_promo_action_later)
}

object ListLabels {

    /*

    <string name="dashboard_tooltip_balance_title">Votre reste à vivre</string>
    <string name="dashboard_tooltip_balance_desc">C'est le montant final dont vous disposez (ou qu'il vous manque) après avoir soustrait l'ensemble de vos dépenses.</string>
    <string name="dashboard_tooltip_balance_due_title">Votre reste à payer</string>
    <string name="dashboard_tooltip_balance_due_desc">C'est le montant de vos dépenses prévisibles qu'il vous reste à payer pour ce mois-ci.</string>
    <string name="dashboard_hero_total_income">Budget</string>
    <string name="dashboard_hero_disposable_income">Restant</string>
    <string name="dashboard_hero_missing_income">Manquant</string>
    <string name="dashboard_hero_total_charges">Total des dépenses</string>
    <string name="dashboard_hero_remaining_to_pay">Dépenses restantes à payer</string>
    <string name="dashboard_tab_all">TOUTES</string>
    <string name="dashboard_tab_paid">PASSÉES</string>
    <string name="dashboard_tab_remaining">À VENIR</string>
    <string name="dashboard_empty_all">Aucune dépense.</string>
    <string name="dashboard_empty_paid">Aucune opération n'a été passée ce mois-ci.</string>
    <string name="dashboard_empty_remaining">Il n'y a pas d'autre opération de prévu ce mois-ci !</string>
    <string name="dashboard_empty_state_desc">Appuyez sur + pour ajouter votre premier abonnement ou dépense récurrente.</string>
    <string name="dashboard_default_name">Sans nom</string>
    <string name="dashboard_due_prefix">Le</string>
    <string name="dashboard_month_all">Tous les mois</string>
    <string name="dashboard_month_1">Janvier</string>
    <string name="dashboard_month_2">Février</string>
    <string name="dashboard_month_3">Mars</string>
    <string name="dashboard_month_4">Avril</string>
    <string name="dashboard_month_5">Mai</string>
    <string name="dashboard_month_6">Juin</string>
    <string name="dashboard_month_7">Juillet</string>
    <string name="dashboard_month_8">Août</string>
    <string name="dashboard_month_9">Septembre</string>
    <string name="dashboard_month_10">Octobre</string>
    <string name="dashboard_month_11">Novembre</string>
    <string name="dashboard_month_12">Décembre</string>

     */

    // Hero Section
    val TOOLTIP_BALANCE_TITLE @Composable get()  = stringResource(Res.string.list_tooltip_balance_title)
    val TOOLTIP_BALANCE_DESC @Composable get()  = stringResource(Res.string.list_tooltip_balance_desc)
    val TOOLTIP_BALANCE_DUE_TITLE @Composable get()  = stringResource(Res.string.list_tooltip_balance_due_title)
    val TOOLTIP_BALANCE_DUE_DESC @Composable get()  = stringResource(Res.string.list_tooltip_balance_due_desc)
    val HERO_TOTAL_INCOME_LABEL @Composable get() = stringResource(Res.string.list_hero_total_income)
    val HERO_DISPOSABLE_INCOME_LABEL @Composable get() = stringResource(Res.string.list_hero_disposable_income)
    val HERO_MISSING_INCOME_LABEL @Composable get() = stringResource(Res.string.list_hero_missing_income)
    val HERO_TOTAL_CHARGES_LABEL @Composable get() = stringResource(Res.string.list_hero_total_charges)
    val HERO_REMAINING_TO_PAY_LABEL @Composable get() = stringResource(Res.string.list_hero_remaining_to_pay)

    // Liste et Filtres
    val TAB_ALL @Composable get() = stringResource(Res.string.list_tab_all)
    val TAB_PAID @Composable get() = stringResource(Res.string.list_tab_paid)
    val TAB_REMAINING @Composable get() = stringResource(Res.string.list_tab_remaining)

    // États de la liste
    val EMPTY_ALL @Composable get() = stringResource(Res.string.list_empty_all)
    val EMPTY_PAID @Composable get() = stringResource(Res.string.list_empty_paid)
    val EMPTY_REMAINING @Composable get() = stringResource(Res.string.list_empty_remaining)
    val EMPTY_STATE_DESC @Composable get() = stringResource(Res.string.list_empty_state_desc)

    val DEFAULT_NAME @Composable get() = stringResource(Res.string.list_default_name)
    val DUE_PREFIX @Composable get() = stringResource(Res.string.list_due_prefix)
    val MONTH_ALL @Composable get() = stringResource(Res.string.list_month_all)

    // Noms des mois
    val MONTH_1 @Composable get() = stringResource(Res.string.list_month_1)
    val MONTH_2 @Composable get() = stringResource(Res.string.list_month_2)
    val MONTH_3 @Composable get() = stringResource(Res.string.list_month_3)
    val MONTH_4 @Composable get() = stringResource(Res.string.list_month_4)
    val MONTH_5 @Composable get() = stringResource(Res.string.list_month_5)
    val MONTH_6 @Composable get() = stringResource(Res.string.list_month_6)
    val MONTH_7 @Composable get() = stringResource(Res.string.list_month_7)
    val MONTH_8 @Composable get() = stringResource(Res.string.list_month_8)
    val MONTH_9 @Composable get() = stringResource(Res.string.list_month_9)
    val MONTH_10 @Composable get() = stringResource(Res.string.list_month_10)
    val MONTH_11 @Composable get() = stringResource(Res.string.list_month_11)
    val MONTH_12 @Composable get() = stringResource(Res.string.list_month_12)

    const val HERO_PAID_LABEL = "Des dépenses payées"
    const val TOOLTIP_TOTAL_INCOME_TITLE = "Votre budget"
    const val TOOLTIP_TOTAL_INCOME_DESC = "C'est le cumule de votre entrée d'argent pour le mois"
}

object SettingsLabels {

    /*

    <string name="settings_section_appearance">Apparence</string>
    <string name="settings_section_support">Soutenir le projet</string>
    <string name="settings_section_data">Données</string>
    <string name="settings_section_account">Compte</string>
    <string name="settings_dark_mode_title">Mode sombre</string>
    <string name="settings_dark_mode_subtitle">Réduire la fatigue visuelle</string>
    <string name="settings_tips_title">Découvrir Outgo</string>
    <string name="settings_tips_subtitle">Présentation du projet, aide et astuces</string>
    <string name="settings_contact_title">Me contacter</string>
    <string name="settings_contact_subtitle">Une question, une idée ou un bug ?</string>
    <string name="settings_sync_title">Synchronisation cloud</string>
    <string name="settings_sync_subtitle">Sauvegardez vos données en toute sécurité</string>
    <string name="settings_logout_title">Se déconnecter</string>
    <string name="settings_logout_subtitle">Se déconnecter de votre compte</string>
    <string name="settings_delete_account">Supprimer mon compte</string>
    <string name="settings_delete_account_subtitle">Cette action est irréversible</string>
    <string name="settings_local_purge_title">Supprimer les données</string>
    <string name="settings_local_purge_subtitle">Réinitialise l'application sur cet appareil.</string>
    <string name="settings_app_version_prefix">Outgo v1.0.0</string>

     */

    val SECTION_APPEARANCE @Composable get() = stringResource(Res.string.settings_section_appearance)
    val SECTION_SUPPORT @Composable get() = stringResource(Res.string.settings_section_support)
    val SECTION_DATA @Composable get() = stringResource(Res.string.settings_section_data)
    val SECTION_ACCOUNT @Composable get() = stringResource(Res.string.settings_section_account)


    val DARK_MODE_TITLE @Composable get() = stringResource(Res.string.settings_dark_mode_title)
    val DARK_MODE_SUBTITLE @Composable get() = stringResource(Res.string.settings_dark_mode_subtitle)
    val TIPS_TITLE @Composable get() = stringResource(Res.string.settings_tips_title)
    val TIPS_SUBTITLE @Composable get() = stringResource(Res.string.settings_tips_subtitle)
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

    const val URL_SITE = "https://abknative.fr"
    const val URL_CONTACT = "https://abknative.fr/contact"

    val APP_VERSION_PREFIX @Composable get() = stringResource(Res.string.settings_app_version_prefix)

    const val SECTION_DATA_AND_ACCOUNT = "Données & Compte"
}

object LoginLabels {

    /*

    <string name="login_title">Bienvenue sur Outgo</string>
    <string name="login_email_label">Email (debug@mail.fr)"</string>
    <string name="login_password_label">Mot de passe (debug)</string>
    <string name="login_submit_button">Se connecter</string>
    <string name="login_back_title">Retour</string>
    <string name="login_error_message">Erreur de connexion</string>

     */

    val TITLE @Composable get() = stringResource(Res.string.login_title)
    val EMAIL_LABEL @Composable get() = stringResource(Res.string.login_email_label)
    val PASSWORD_LABEL @Composable get() = stringResource(Res.string.login_password_label)
    val SUBMIT_BUTTON @Composable get() = stringResource(Res.string.login_submit_button)
    val BACK_TITLE @Composable get() = stringResource(Res.string.login_back_title)
    val ERROR_MESSAGE @Composable get() = stringResource(Res.string.login_error_message)

    // --- Temporaire ---
    const val REGISTER_ACTION = " Pas encore de compte ? S'inscrire"
    const val CONFLICT_TITLE = "Sauvegarde existante"
    const val CONFLICT_DESC = "Ce compte contient déjà des données sur nos serveurs. En vous connectant, vos données actuelles seront mises de côté pour afficher celles de votre compte en ligne."
    const val CONFLICT_QUESTION = "Que voulez-vous faire ?"
    const val CONFLICT_CONFIRM = "Télécharger le Cloud"
    const val CONFLICT_CANCEL = "Annuler la connexion"
}
