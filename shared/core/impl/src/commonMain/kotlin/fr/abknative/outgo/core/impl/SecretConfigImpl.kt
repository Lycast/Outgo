package fr.abknative.outgo.core.impl

import fr.abknative.outgo.core.api.SecretConfig
import fr.abknative.outgo.core.impl.secret.OutgoConfig

internal class SecretConfigImpl : SecretConfig {
    override val baseUrl: String = OutgoConfig.BASE_URL
    override val webClientId: String = OutgoConfig.WEB_CLIENT_ID
}