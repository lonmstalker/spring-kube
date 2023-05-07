package io.lonmstalker.springkube.mapper

import io.lonmstalker.springkube.dto.RegUserRequestDto
import io.lonmstalker.springkube.dto.UserInfoDto
import io.lonmstalker.springkube.enums.UserRole
import io.lonmstalker.springkube.enums.UserStatus
import io.lonmstalker.springkube.model.RegUser
import io.lonmstalker.springkube.model.User
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    imports = [UUID::class, UserRole::class, UserStatus::class]
)
@JvmDefaultWithCompatibility
interface UserMapper {

    @Mapping(target = "invitedBy", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "userGroupId", ignore = true)
    @Mapping(target = "userPasswordId", ignore = true)
    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "role", expression = "java(UserRole.GROUP_ADMIN)")
    @Mapping(target = "status", expression = "java(UserStatus.ACTIVATED)")
    fun toModel(regUser: RegUserRequestDto): RegUser

    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "userGroupId", ignore = true)
    @Mapping(target = "userPasswordId", ignore = true)
    @Mapping(target = "role", expression = "java(UserRole.USER)")
    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "status", expression = "java(UserStatus.ACTIVATED)")
    fun toModel(regUser: RegUserRequestDto, invitedBy: UUID): RegUser

    @Mapping(target = "loginAttempts", ignore = true)
    @Mapping(source = "userGroupId",target = "userGroupId")
    fun toUser(regUser: RegUser, userGroupId: UUID): User

    fun toDto(regUser: User): UserInfoDto

    fun map(date: OffsetDateTime?): LocalDateTime? = date?.toLocalDateTime()

    fun map(date: LocalDateTime?): OffsetDateTime? = date?.atOffset(ZoneOffset.UTC)
}