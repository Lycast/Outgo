package fr.abknative.outgo.android.ui

// Fichier : CommonLabels.kt
object CommonLabels {
    const val APP_NAME = "OUTGO"
    const val CURRENCY_SYMBOL = "€"
    const val ACTION_ADD = "Ajouter"
    const val ACTION_SAVE = "Enregistrer"
    const val ACTION_CANCEL = "Annuler"
    const val ACTION_DELETE = "Supprimer"
    const val ACTION_EDIT = "Éditer"
    const val ERROR_GENERIC = "Une erreur est survenue"
}

// Fichier : DashboardLabels.kt
object DashboardLabels {
    // Hero Section
    const val HERO_INCOME_LABEL = "Mon Revenu"
    const val HERO_BUDGET_EMPTY = "Entrer votre budget mensuel"
    const val HERO_DISPOSABLE_INCOME_LABEL = "Reste à vivre"
    const val HERO_TOTAL_CHARGES_LABEL = "Total des charges"
    const val HERO_REMAINING_TO_PAY_LABEL = "Reste à prélever"
    const val HERO_DATE_PREFIX = "Date du jour :"

    // Liste et Filtres
    const val SECTION_EXPENSES_TITLE = "Liste des dépenses"
    const val TAB_ALL = "TOUTES"
    const val TAB_PAID = "PAYÉES"
    const val TAB_REMAINING = "RESTANTES"

    // États de la liste
    const val EMPTY_STATE_TITLE = "Aucune dépense"
    const val EMPTY_STATE_DESC = "Appuyez sur + pour ajouter votre premier abonnement ou dépense récurrente."
}

// Fichier : NavigationLabels.kt
object NavigationLabels {
    const val TAB_HOME = "Accueil"
    const val TAB_STATS = "Stats"
    const val TAB_SETTINGS = "Paramètres"
}

// Fichier : OutgoingFormContent.kt (Pour le BottomSheet)
object FormLabels {
    const val SHEET_TITLE_ADD = "Nouvelle dépense"
    const val SHEET_TITLE_EDIT = "Modifier la dépense"
    const val FIELD_NAME = "Titre de la dépense"
    const val FIELD_AMOUNT = "Montant"
    const val FIELD_DATE = "Jour de prélèvement"
    const val FIELD_MONTH = "Mois de prélèvement (1-12)"
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