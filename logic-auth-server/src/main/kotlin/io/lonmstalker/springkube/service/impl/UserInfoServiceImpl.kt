package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.constants.ErrorConstants.EMAIL_EXISTS
import io.lonmstalker.springkube.constants.ErrorConstants.USERNAME_EXISTS
import io.lonmstalker.springkube.exception.AuthException
import io.lonmstalker.springkube.model.User
import io.lonmstalker.springkube.model.UserPassword
import io.lonmstalker.springkube.repository.UserInfoRepository
import io.lonmstalker.springkube.service.PasswordService
import io.lonmstalker.springkube.service.UserGroupService
import io.lonmstalker.springkube.service.UserInfoService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserInfoServiceImpl(
    private val passwordService: PasswordService,
    private val userGroupService: UserGroupService,
    private val userInfoRepository: UserInfoRepository,
) : UserInfoService {

    @Transactional
    override fun save(user: User, password: String): User {
        this.validateLogin(user)

        val userInfo = this.userInfoRepository.insert(user)
        val userGroup = this.userGroupService.saveBy(user.username, userInfo.id)
        val password = this.passwordService.save(UserPassword(userId = userInfo.id, value = password))

        return userInfo
            .copy(userPasswordId = password.id)
            .let { this.userInfoRepository.update(it, userGroup.id) }
    }

    private fun validateLogin(user: User) {
        if (this.userInfoRepository.existsEmail(user.email)) {
            throw AuthException(EMAIL_EXISTS, "user with email ${user.email} already registered")
        }
        if (this.userInfoRepository.existsUsername(user.username)) {
            throw AuthException(USERNAME_EXISTS, "user with username ${user.username} already registered")
        }
    }
}