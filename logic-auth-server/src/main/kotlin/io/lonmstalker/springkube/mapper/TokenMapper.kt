package io.lonmstalker.springkube.mapper

import io.lonmstalker.springkube.dto.TokenResponseDto
import io.lonmstalker.springkube.model.UserTokenInfo
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import java.util.*

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    imports = [UUID::class]
)
@JvmDefaultWithCompatibility
interface TokenMapper {

    fun toDto(userTokenInfo: UserTokenInfo): TokenResponseDto
}