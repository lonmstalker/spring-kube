package io.lonmstalker.springkube.converter

import io.lonmstalker.springkube.model.User

interface TokenConverter {
    fun convertToken(): User
    fun support(clientId: String): Boolean
}