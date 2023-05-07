package io.lonmstalker.springkube.mapper

import io.lonmstalker.springkube.dto.TokenResponseDto
import io.lonmstalker.springkube.model.UserTokenInfo
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import java.time.Instant
import java.util.concurrent.TimeUnit

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
@JvmDefaultWithCompatibility
interface TokenMapper {

    fun toDto(userTokenInfo: UserTokenInfo): TokenResponseDto

    fun map(instant: Instant): Long = instant.epochSecond - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
}