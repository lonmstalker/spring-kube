package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.exception.ObjectNotFoundException
import io.lonmstalker.springkube.model.UserPassword
import io.lonmstalker.springkube.repository.PasswordRepository
import io.lonmstalker.springkube.service.PasswordService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class PasswordServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val passwordRepository: PasswordRepository
) : PasswordService {

    override fun findById(id: UUID): UserPassword =
        this.passwordRepository
            .findById(id)
            ?: throw ObjectNotFoundException(id)

    override fun save(password: UserPassword): UserPassword =
        this.passwordRepository.insert(password.copy(value = this.passwordEncoder.encode(password.value)))
}