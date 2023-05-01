package io.lonmstalker.springkube.utils.internal

import java.util.Locale

internal object ThreadLocalStorage {
    private val locale: ThreadLocal<Locale> = ThreadLocal()

    fun setLocal(locale: Locale) = this.locale.set(locale)
    fun getLocal(): Locale = this.locale.get() ?: Locale.getDefault()

    fun removeLocal() = this.locale.remove()
}