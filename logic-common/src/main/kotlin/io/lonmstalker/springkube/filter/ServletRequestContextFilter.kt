package io.lonmstalker.springkube.filter

import io.lonmstalker.springkube.model.system.ServletRequestContext
import io.lonmstalker.springkube.utils.internal.ThreadLocalStorage
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.LocaleResolver

class ServletRequestContextFilter(private val localeResolver: LocaleResolver) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            ThreadLocalStorage.setLocal(this.localeResolver.resolveLocale(request))
            ThreadLocalStorage.setCtx(ServletRequestContext(request, response))
            filterChain.doFilter(request, response)
        } finally {
            ThreadLocalStorage.removeCtx()
            ThreadLocalStorage.removeLocal()
        }
    }
}