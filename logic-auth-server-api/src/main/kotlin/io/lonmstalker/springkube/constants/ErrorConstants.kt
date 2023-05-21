package io.lonmstalker.springkube.constants

object ErrorConstants {
    const val USER_IN_BLOCK = "user.blocked"
    const val EMAIL_EXISTS = "user.email_exists"
    const val USERNAME_EXISTS = "user.username_exists"
    const val USER_BAD_CREDENTIALS = "user.bad_credentials"
    const val USER_NO_PASSWORD = "user.no_password"
    const val USERNAME_NOT_EXISTS = "user.username_not_exists"
    const val ID_NOT_EXISTS = "user.id_not_exists"
    const val OAUTH2_GRANT_TYPE_UNKNOWN = "authentication.grant_type.unknown"
    const val OAUTH2_BAD_REQUEST = "authentication.params.invalid"
    const val OAUTH2_TOKEN_NOT_FOUND = "authentication.token.not_found"
    const val OAUTH2_PROVIDER_DISABLED = "authentication.provider.disabled"
    const val OAUTH2_PROVIDER_UNKNOWN = "authentication.provider.unknown"
}