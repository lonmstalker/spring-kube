package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.config.properties.AuthProperties
import io.lonmstalker.springkube.exception.ObjectNotFoundException
import io.lonmstalker.springkube.model.UserGroup
import io.lonmstalker.springkube.repository.UserGroupRepository
import io.lonmstalker.springkube.service.UserGroupService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserGroupServiceImpl(
    private val authProperties: AuthProperties,
    private val groupRepository: UserGroupRepository,
) : UserGroupService {

    override fun findById(id: UUID): UserGroup =
        this.groupRepository
            .findByGroupById(id)
            ?: throw ObjectNotFoundException(id)

    override fun saveBy(username: String, userId: UUID): UserGroup =
        this.groupRepository
            .insert(
                UserGroup(
                    title = this.getDefaultTitle(username),
                    inviteLink = this.getDefaultInviteLink(userId),
                    createdBy = userId
                )
            )

    override fun update(userGroup: UserGroup): UserGroup =
        this.groupRepository.update(userGroup)

    private fun getDefaultTitle(username: String) = "Группы пользователя $username"

    private fun getDefaultInviteLink(userId: UUID) = this.authProperties.inviteLink + "/$userId"
}