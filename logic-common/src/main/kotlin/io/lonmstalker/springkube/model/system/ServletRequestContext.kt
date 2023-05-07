package io.lonmstalker.springkube.model.system

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

data class ServletRequestContext(
    val request: HttpServletRequest,
    val response: HttpServletResponse,
)