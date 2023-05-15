package io.lonmstalker.springkube.model

import io.lonmstalker.springkube.enums.OidcProvider
import java.util.UUID

data class UserProvider(
    val id: UUID = UUID.randomUUID(),
    val provider: OidcProvider,
    val userId: UUID,
    val providerUserId: String,
    val username: String,
    val enabled: Boolean = true,
    val user: User? = null
)
