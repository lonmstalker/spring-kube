package io.lonmstalker.springkube.config.security

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import java.security.Security

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
class BouncyCastleInitConfig {

    companion object {
        init {
            Security.addProvider(BouncyCastleProvider())
        }
    }
}