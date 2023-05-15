package io.lonmstalker.springkube.exception

import io.lonmstalker.springkube.constants.ErrorCodes.INTERNAL_SERVER_ERROR

class DefaultException(msg: String) : BaseException(INTERNAL_SERVER_ERROR, msg)