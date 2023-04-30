package io.lonmstalker.springkube.exception

import io.lonmstalker.springkube.constants.ErrorCodes.OBJECT_NOT_FOUND
import java.util.UUID

class ObjectNotFoundException(id: UUID) : BaseException("object not found", OBJECT_NOT_FOUND, arrayOf(id))