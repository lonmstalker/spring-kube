package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.enums.OidcProvider
import io.lonmstalker.springkube.model.UserProvider
import io.lonmstalker.springkube.repository.UserProviderRepository
import io.lonmstalker.springkube.service.UserProviderService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserProviderServiceImpl(private val userProviderRepository: UserProviderRepository) : UserProviderService {

    override fun insert(userProvider: UserProvider): UserProvider =
        this.userProviderRepository.insert(userProvider)

    override fun delete(userId: UUID, userProvider: OidcProvider): Int =
        this.userProviderRepository.delete(userId, userProvider.name)

    override fun findByProviderAndProviderUserId(providerUserId: String, userProvider: OidcProvider): UserProvider? =
        this.userProviderRepository
            .findByProviderAndProviderUserId(providerUserId, userProvider.name)
}