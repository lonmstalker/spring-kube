package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.constants.ErrorConstants.EMAIL_EXISTS
import io.lonmstalker.springkube.constants.ErrorConstants.ID_NOT_EXISTS
import io.lonmstalker.springkube.constants.ErrorConstants.USERNAME_EXISTS
import io.lonmstalker.springkube.constants.ErrorConstants.USERNAME_NOT_EXISTS
import io.lonmstalker.springkube.enums.UserStatus
import io.lonmstalker.springkube.exception.AuthException
import io.lonmstalker.springkube.model.RegUser
import io.lonmstalker.springkube.model.User
import io.lonmstalker.springkube.model.UserGroup
import io.lonmstalker.springkube.model.UserPassword
import io.lonmstalker.springkube.repository.UserInfoRepository
import io.lonmstalker.springkube.service.PasswordService
import io.lonmstalker.springkube.service.UserGroupService
import io.lonmstalker.springkube.service.UserInfoService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional(readOnly = true)
class UserInfoServiceImpl(
    private val passwordService: PasswordService,
    private val userGroupService: UserGroupService,
    private val userInfoRepository: UserInfoRepository,
) : UserInfoService {

    @Transactional
    override fun save(regUser: RegUser, password: String): User {
        val userGroup = this.saveWithoutPassword(regUser)
        val pswd = this.passwordService.save(UserPassword(userId = userGroup.first.id, value = password))

        return userGroup.first
            .copy(userGroupId = userGroup.second.id, userPasswordId = pswd.id)
            .let { this.userInfoRepository.update(it) }
    }

    @Transactional
    override fun saveWithoutPassword(regUser: RegUser): Pair<User, UserGroup> {
        this.validateLogin(regUser.username, regUser.email)
        val login = regUser.username ?: regUser.email

        val userInfo = this.userInfoRepository.insert(regUser.copy(username = login))
        val userGroup = this.userGroupService.saveBy(login, userInfo.id)

        return userInfo to userGroup
    }

    override fun findByUsername(username: String): User =
        this.userInfoRepository
            .findByUsername(username)
            ?: throw AuthException(USERNAME_NOT_EXISTS, "user with username $username not found")

    override fun findById(id: UUID): User =
        this.userInfoRepository
            .findById(id)
            ?: throw AuthException(ID_NOT_EXISTS, "user with id $id not found")

    @Transactional
    override fun incrementLoginAttempts(username: String) =
        this.userInfoRepository.incrementLoginAttempts(username)

    @Transactional
    override fun updateStatus(id: UUID, status: UserStatus) =
        this.userInfoRepository.updateStatus(id, status.name)

    @Transactional
    override fun lockUser(id: UUID, time: LocalDateTime) =
        this.userInfoRepository.lockUser(id, time)

    @Transactional
    override fun unlockUser(id: UUID) =
        this.userInfoRepository.unlockUser(id)

    private fun validateLogin(username: String?, email: String) {
        if (this.userInfoRepository.existsEmail(email)) {
            throw AuthException(EMAIL_EXISTS, "user with email $email already registered")
        }
        if (username != null && this.userInfoRepository.existsUsername(username)) {
            throw AuthException(USERNAME_EXISTS, "user with username $username already registered")
        }
    }
}