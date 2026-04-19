package com.dreifus.app.data.notes

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

actual object PinHasher {

    private const val ITERATIONS = 100_000
    private const val KEY_BITS = 256
    private const val SALT_BYTES = 16

    actual fun hash(pin: String): String {
        val salt = ByteArray(SALT_BYTES).also { SecureRandom().nextBytes(it) }
        val hash = pbkdf2(pin, salt)
        return "${salt.toHex()}:${hash.toHex()}"
    }

    actual fun verify(pin: String, stored: String): Boolean {
        if (stored.isEmpty()) return false
        val parts = stored.split(":")
        if (parts.size != 2) return false
        return runCatching {
            val salt = parts[0].fromHex()
            val expected = parts[1].fromHex()
            pbkdf2(pin, salt).contentEquals(expected)
        }.getOrDefault(false)
    }

    private fun pbkdf2(pin: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(pin.toCharArray(), salt, ITERATIONS, KEY_BITS)
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            .generateSecret(spec)
            .encoded
    }
}

private fun ByteArray.toHex() = joinToString("") { "%02x".format(it) }
private fun String.fromHex() = chunked(2).map { it.toInt(16).toByte() }.toByteArray()
