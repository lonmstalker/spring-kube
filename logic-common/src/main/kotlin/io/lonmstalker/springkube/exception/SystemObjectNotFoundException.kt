package io.lonmstalker.springkube.exception

import io.lonmstalker.springkube.constants.ErrorCodes.OBJECT_NOT_FOUND
import java.util.UUID

class SystemObjectNotFoundException(value: String) : BaseException("object not found", OBJECT_NOT_FOUND, arrayOf(value))