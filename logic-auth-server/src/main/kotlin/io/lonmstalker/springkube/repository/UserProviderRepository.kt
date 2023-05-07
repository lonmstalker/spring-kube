package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.UserProvider
import java.util.*

interface UserProviderRepository {
    fun delete(userId: UUID, userProvider: String): Int
    fun insert(userProvider: UserProvider): UserProvider
    fun findByProviderAndProviderUserId(providerUserId: String, userProvider: String): UserProvider?
}