package com.dreifus.app.data.notes

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CCCrypt
import platform.CoreCrypto.CCHmac
import platform.CoreCrypto.CCKeyDerivationPBKDF
import platform.CoreCrypto.kCCAlgorithmAES128
import platform.CoreCrypto.kCCDecrypt
import platform.CoreCrypto.kCCEncrypt
import platform.CoreCrypto.kCCHmacAlgSHA256
import platform.CoreCrypto.kCCOptionPKCS7Padding
import platform.CoreCrypto.kCCPBKDF2
import platform.CoreCrypto.kCCPRFHmacAlgSHA256
import platform.Security.SecRandomCopyBytes
import platform.Security.kSecRandomDefault

// AES-256-CBC + HMAC-SHA256 (Encrypt-then-MAC) via CommonCrypto.
// Two 32-byte sub-keys are derived from one PBKDF2 call: enc key + mac key.
@OptIn(ExperimentalForeignApi::class)
actual object NoteEncryptor {

    private const val ITERATIONS = 100_000u
    private const val SALT_BYTES = 16
    private const val IV_BYTES = 16
    private const val ENC_KEY_BYTES = 32
    private const val MAC_KEY_BYTES = 32
    private const val HMAC_BYTES = 32

    actual fun encrypt(plaintext: String, pin: String): EncryptedData {
        val salt = generateRandom(SALT_BYTES)
        val iv = generateRandom(IV_BYTES)
        val (encKey, macKey) = deriveKeys(pin, salt)
        val ciphertext = aesCbcEncrypt(encKey, iv, plaintext.encodeToByteArray())
        val tag = hmacSha256(macKey, iv + ciphertext)
        return EncryptedData(ciphertext = ciphertext + tag, iv = iv, salt = salt)
    }

    actual fun decrypt(ciphertext: ByteArray, iv: ByteArray, salt: ByteArray, pin: String): String? {
        if (ciphertext.size <= HMAC_BYTES) return null
        return runCatching {
            val (encKey, macKey) = deriveKeys(pin, salt)
            val actualCt = ciphertext.copyOf(ciphertext.size - HMAC_BYTES)
            val storedTag = ciphertext.copyOfRange(ciphertext.size - HMAC_BYTES, ciphertext.size)
            val expectedTag = hmacSha256(macKey, iv + actualCt)
            if (!expectedTag.contentEquals(storedTag)) return null
            aesCbcDecrypt(encKey, iv, actualCt)?.decodeToString()
        }.getOrNull()
    }

    private fun deriveKeys(pin: String, salt: ByteArray): Pair<ByteArray, ByteArray> {
        val password = pin.encodeToByteArray()
        val derived = ByteArray(ENC_KEY_BYTES + MAC_KEY_BYTES)
        password.usePinned { pwdPinned ->
            salt.usePinned { saltPinned ->
                derived.usePinned { derivedPinned ->
                    CCKeyDerivationPBKDF(
                        kCCPBKDF2.convert(),
                        pwdPinned.addressOf(0).reinterpret(),
                        password.size.convert(),
                        saltPinned.addressOf(0).reinterpret(),
                        salt.size.convert(),
                        kCCPRFHmacAlgSHA256.convert(),
                        ITERATIONS,
                        derivedPinned.addressOf(0).reinterpret(),
                        (ENC_KEY_BYTES + MAC_KEY_BYTES).convert(),
                    )
                }
            }
        }
        return derived.copyOf(ENC_KEY_BYTES) to derived.copyOfRange(ENC_KEY_BYTES, ENC_KEY_BYTES + MAC_KEY_BYTES)
    }

    private fun generateRandom(size: Int): ByteArray {
        val buf = ByteArray(size)
        buf.usePinned { pinned ->
            SecRandomCopyBytes(kSecRandomDefault, size.convert(), pinned.addressOf(0))
        }
        return buf
    }

    private fun aesCbcEncrypt(key: ByteArray, iv: ByteArray, data: ByteArray): ByteArray {
        val bufSize = data.size + 16
        val buf = ByteArray(bufSize)
        val moved = memScoped {
            val movedVar = alloc<ULongVar>()
            key.usePinned { k ->
                iv.usePinned { i ->
                    data.usePinned { d ->
                        buf.usePinned { b ->
                            CCCrypt(
                                kCCEncrypt.convert(),
                                kCCAlgorithmAES128.convert(),
                                kCCOptionPKCS7Padding.convert(),
                                k.addressOf(0),
                                ENC_KEY_BYTES.convert(),
                                i.addressOf(0),
                                d.addressOf(0),
                                data.size.convert(),
                                b.addressOf(0),
                                bufSize.convert(),
                                movedVar.ptr,
                            )
                        }
                    }
                }
            }
            movedVar.value.toInt()
        }
        return buf.copyOf(moved)
    }

    private fun aesCbcDecrypt(key: ByteArray, iv: ByteArray, data: ByteArray): ByteArray? {
        val bufSize = data.size
        val buf = ByteArray(bufSize)
        var decryptedLen = 0
        val success = memScoped {
            val movedVar = alloc<ULongVar>()
            val status = key.usePinned { k ->
                iv.usePinned { i ->
                    data.usePinned { d ->
                        buf.usePinned { b ->
                            CCCrypt(
                                kCCDecrypt.convert(),
                                kCCAlgorithmAES128.convert(),
                                kCCOptionPKCS7Padding.convert(),
                                k.addressOf(0),
                                ENC_KEY_BYTES.convert(),
                                i.addressOf(0),
                                d.addressOf(0),
                                data.size.convert(),
                                b.addressOf(0),
                                bufSize.convert(),
                                movedVar.ptr,
                            )
                        }
                    }
                }
            }
            decryptedLen = movedVar.value.toInt()
            status == 0
        }
        return if (success) buf.copyOf(decryptedLen) else null
    }

    private fun hmacSha256(key: ByteArray, data: ByteArray): ByteArray {
        val digest = ByteArray(HMAC_BYTES)
        key.usePinned { k ->
            data.usePinned { d ->
                digest.usePinned { dig ->
                    CCHmac(
                        kCCHmacAlgSHA256.convert(),
                        k.addressOf(0),
                        key.size.convert(),
                        d.addressOf(0),
                        data.size.convert(),
                        dig.addressOf(0),
                    )
                }
            }
        }
        return digest
    }
}
