package com.dreifus.app.data.notes

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CCKeyDerivationPBKDF
import platform.CoreCrypto.kCCPBKDF2
import platform.CoreCrypto.kCCPRFHmacAlgSHA256
import platform.Security.SecRandomCopyBytes
import platform.Security.kSecRandomDefault

@OptIn(ExperimentalForeignApi::class)
actual object PinHasher {

    private const val ITERATIONS = 100_000u
    private const val KEY_BYTES = 32
    private const val SALT_BYTES = 16

    actual fun hash(pin: String): String {
        val salt = generateSalt()
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

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(SALT_BYTES)
        salt.usePinned { pinned ->
            SecRandomCopyBytes(kSecRandomDefault, SALT_BYTES.convert(), pinned.addressOf(0))
        }
        return salt
    }

    private fun pbkdf2(pin: String, salt: ByteArray): ByteArray {
        val password = pin.encodeToByteArray()
        val derivedKey = ByteArray(KEY_BYTES)
        password.usePinned { pwdPinned ->
            salt.usePinned { saltPinned ->
                derivedKey.usePinned { keyPinned ->
                    CCKeyDerivationPBKDF(
                        kCCPBKDF2.convert(),
                        pwdPinned.addressOf(0).reinterpret(),
                        password.size.convert(),
                        saltPinned.addressOf(0).reinterpret(),
                        salt.size.convert(),
                        kCCPRFHmacAlgSHA256.convert(),
                        ITERATIONS,
                        keyPinned.addressOf(0).reinterpret(),
                        KEY_BYTES.convert(),
                    )
                }
            }
        }
        return derivedKey
    }
}

private fun ByteArray.toHex() = joinToString("") { "%02x".format(it) }
private fun String.fromHex() = chunked(2).map { it.toInt(16).toByte() }.toByteArray()
