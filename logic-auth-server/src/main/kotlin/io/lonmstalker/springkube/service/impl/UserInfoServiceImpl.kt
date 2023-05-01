package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.constants.ErrorConstants.EMAIL_EXISTS
import io.lonmstalker.springkube.constants.ErrorConstants.USERNAME_EXISTS
import io.lonmstalker.springkube.constants.ErrorConstants.USERNAME_NOT_EXISTS
import io.lonmstalker.springkube.exception.AuthException
import io.lonmstalker.springkube.mapper.UserMapper
import io.lonmstalker.springkube.model.RegUser
import io.lonmstalker.springkube.model.User
import io.lonmstalker.springkube.model.UserPassword
import io.lonmstalker.springkube.repository.UserInfoRepository
import io.lonmstalker.springkube.service.PasswordService
import io.lonmstalker.springkube.service.UserGroupService
import io.lonmstalker.springkube.service.UserInfoService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserInfoServiceImpl(
    private val userMapper: UserMapper,
    private val passwordService: PasswordService,
    private val userGroupService: UserGroupService,
    private val userInfoRepository: UserInfoRepository,
) : UserInfoService {

    @Transactional
    override fun save(regUser: RegUser, password: String): User {
        this.validateLogin(regUser.username, regUser.email)
        val login = regUser.username ?: regUser.email

        val userInfo = this.userInfoRepository.insert(regUser)
        val userGroup = this.userGroupService.saveBy(login, userInfo.id)
        val pswd = this.passwordService.save(UserPassword(userId = userInfo.id, value = password))

        return userInfo
            .copy(userPasswordId = pswd.id, username = login)
            .let { this.userInfoRepository.update(this.userMapper.toUser(it, userGroup.id)) }
    }

    override fun findByUsername(username: String): User =
        this.userInfoRepository
            .findByUsername(username)
            ?: throw AuthException(USERNAME_NOT_EXISTS, "user with username $username not found")

    private fun validateLogin(username: String?, email: String) {
        if (this.userInfoRepository.existsEmail(email)) {
            throw AuthException(EMAIL_EXISTS, "user with email $email already registered")
        }
        if (username != null && this.userInfoRepository.existsUsername(username)) {
            throw AuthException(USERNAME_EXISTS, "user with username $username already registered")
        }
    }
}