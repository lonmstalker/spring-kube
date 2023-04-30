package io.lonmstalker.springkube.model

import java.util.UUID

data class UserInfo (
    val userId: UUID,
    val userGroupId: UUID
)