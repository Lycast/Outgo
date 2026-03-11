package fr.abknative.outgo.android.ui

// Fichier : CommonLabels.kt
object CommonLabels {
    const val APP_NAME = "OUTGO"
    const val CURRENCY_SYMBOL = "€"
    const val ACTION_ADD = "Ajouter"
    const val ACTION_SAVE = "Enregistrer"
    const val ACTION_CANCEL = "Annuler"
    const val ACTION_DELETE = "Supprimer"
}

// Fichier : DashboardLabels.kt
object DashboardLabels {
    // Hero Section
    const val HERO_INCOME_LABEL = "Mon Budget"
    const val HERO_DISPOSABLE_INCOME_LABEL = "Reste à vivre"
    const val HERO_TOTAL_CHARGES_LABEL = "Total des charges"
    const val HERO_REMAINING_TO_PAY_LABEL = "Reste à payer"
    const val HERO_DATE_PREFIX = "Budget de"

    // Liste et Filtres
    const val TAB_ALL = "TOUTES"
    const val TAB_PAID = "PAYÉES"
    const val TAB_REMAINING = "RESTANTES"

    // États de la liste
    const val EMPTY_ALL = "Aucune dépense."
    const val EMPTY_PAID = "Vous n'avez pas encore réglé de dépenses."
    const val EMPTY_REMAINING = "Félicitations, tout est payé !"
    const val EMPTY_STATE_DESC = "Appuyez sur + pour ajouter votre premier abonnement ou dépense récurrente."
}

// Fichier : OutgoingFormContent.kt (Pour le BottomSheet)
object FormLabels {
    const val SHEET_TITLE_ADD = "Nouvelle dépense"
    const val SHEET_TITLE_EDIT = "Modifier la dépense"
    const val FIELD_NAME = "Nom de la dépense"
    const val FIELD_AMOUNT = "Montant"
    const val FIELD_DATE = "Jour (1-31)"
    const val FIELD_MONTH = "Mois (1-12)"
    const val CYCLE_MONTHLY = "Mensuel"
    const val CYCLE_YEARLY = "Annuel"
}

// Fichier : Header.kt + SyncPromotionModal.kt
object HeaderLabels {
    const val SYNC_PROMO_TITLE = "Sauvegardez vos données"
    const val SYNC_PROMO_DESC = "Créez un compte gratuit pour synchroniser vos abonnements et ne jamais perdre vos données, même si vous changez de téléphone."
    const val SYNC_PROMO_ACTION_LOGIN = "Créer un compte"
    const val SYNC_PROMO_ACTION_LATER = "Plus tard"
}

// Fichier : BudgetEditDialog.kt
object BudgetEditDialogLabels {
    const val DIALOG_BUDGET_TITLE = "Mon budget mensuel"
    const val DIALOG_BUDGET_DESC = "Saisissez le montant total alloué à vos dépenses fixes ce mois-ci."
    const val DIALOG_BUDGET_FIELD = "Montant du budget"
}

// Labels d'accessibilité des composants
object AccessibilityLabels {
    // --- Header & Synchronisation ---
    const val SYNCED = "Synchronisé avec le serveur"
    const val NOT_SYNCED = "Non synchronisé, appuyez pour configurer"
    const val SYNC_ICON = "Icône de statut de synchronisation"

    // --- Actions ---
    const val ADD_EXPENSE = "Ajouter une nouvelle dépense"
    const val CLOSE_DIALOG = "Fermer la boîte de dialogue"

    // --- États ---
    const val INFO_EMPTY_STATE = "Information : liste vide"
}

// Dans DashboardLabels.kt ou un nouveau fichier SettingsLabels.kt
object SettingsLabels {
    const val CHEVRON_DESC = "Ouvrir"

    // Sections
    const val SECTION_APPEARANCE = "Apparence"
    const val SECTION_SUPPORT = "Soutenir le projet"
    const val SECTION_DATA = "Données"

    // Apparence
    const val DARK_MODE_TITLE = "Mode Sombre"
    const val DARK_MODE_SUBTITLE = "Réduire la fatigue visuelle"

    // Soutien
    const val COFFEE_TITLE = "Offrir un café ☕"
    const val COFFEE_SUBTITLE = "Aidez à maintenir l'application gratuite"
    const val TIPS_TITLE = "Astuces & Aide"
    const val TIPS_SUBTITLE = "Découvrir comment optimiser son budget"

    // Données
    const val SYNC_TITLE = "Synchronisation Cloud"
    const val SYNC_SUBTITLE = "Sauvegardez vos données en toute sécurité"

    // Footer
    const val APP_VERSION_PREFIX = "Outgo v1.0.0"
}