package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.enums.OidcProvider
import io.lonmstalker.springkube.model.UserProvider
import java.util.*

interface UserProviderService {
    fun insert(userProvider: UserProvider): UserProvider
    fun delete(userId: UUID, userProvider: OidcProvider): Int
    fun findByProviderAndProviderUserId(providerUserId: String, userProvider: OidcProvider): UserProvider?
}