package io.lonmstalker.springkube.repository.impl

import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.model.UserPassword
import io.lonmstalker.springkube.repository.PasswordRepository
import io.lonmstalker.springkube.tables.UserPasswordTable
import io.lonmstalker.springkube.tables.UserPasswordTable.toPassword
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class PasswordRepositoryImpl(private val clockHelper: ClockHelper) : PasswordRepository {

    override fun findLastById(id: UUID): UserPassword? =
        UserPasswordTable
            .select { UserPasswordTable.id eq id }
            .firstOrNull()
            ?.toPassword()

    override fun findLastByUser(userId: UUID, lastCount: Int): List<UserPassword> =
        UserPasswordTable
            .select { UserPasswordTable.userId eq userId }
            .orderBy(UserPasswordTable.createdDate)
            .limit(lastCount)
            .map { it.toPassword() }

    override fun insert(password: UserPassword): UserPassword =
        UserPasswordTable
            .insert {
                it[id] = password.id
                it[value] = password.value
                it[userId] = password.userId
                it[type] = password.type.name
                it[createdDate] = clockHelper.clock()
            }
            .resultedValues!![0]
            .toPassword()
}