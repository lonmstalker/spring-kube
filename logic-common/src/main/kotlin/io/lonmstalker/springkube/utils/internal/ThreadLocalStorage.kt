package io.lonmstalker.springkube.utils.internal

import io.lonmstalker.springkube.model.system.ServletRequestContext
import java.util.Locale

internal object ThreadLocalStorage {
    private val locale: ThreadLocal<Locale> = ThreadLocal()
    private val ctx: ThreadLocal<ServletRequestContext> = ThreadLocal()

    fun setLocal(locale: Locale) = this.locale.set(locale)
    fun getLocal(): Locale = this.locale.get() ?: Locale.getDefault()

    fun removeLocal() = this.locale.remove()

    fun setCtx(ctx: ServletRequestContext) = this.ctx.set(ctx)
    fun getCtx(): ServletRequestContext = this.ctx.get()

    fun removeCtx() = this.ctx.remove()
}