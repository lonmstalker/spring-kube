package io.lonmstalker.springkube.exception

class AuthNoRollbackException(code0: String, message0: String, args: Array<Any>?) : BaseException(message0, code0, args) {
    constructor(code0: String, message0: String) : this(code0, message0, null)
}