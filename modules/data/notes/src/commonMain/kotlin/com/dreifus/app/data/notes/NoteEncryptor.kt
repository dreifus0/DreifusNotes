package com.dreifus.app.data.notes

data class EncryptedData(
    val ciphertext: ByteArray,
    val iv: ByteArray,
    val salt: ByteArray,
)

expect object NoteEncryptor {
    fun encrypt(plaintext: String, pin: String): EncryptedData
    fun decrypt(ciphertext: ByteArray, iv: ByteArray, salt: ByteArray, pin: String): String?
}

private fun ByteArray.toHex() = joinToString("") { "%02x".format(it) }
private fun String.fromHex() = chunked(2).map { it.toInt(16).toByte() }.toByteArray()

private const val ENC_PREFIX = "ENC:v1:"

fun EncryptedData.encodeToString(): String =
    "$ENC_PREFIX${ciphertext.toHex()}|${iv.toHex()}|${salt.toHex()}"

fun String.decodeEncryptedData(): EncryptedData? {
    if (!startsWith(ENC_PREFIX)) return null
    val parts = removePrefix(ENC_PREFIX).split("|")
    if (parts.size != 3) return null
    return runCatching {
        EncryptedData(
            ciphertext = parts[0].fromHex(),
            iv = parts[1].fromHex(),
            salt = parts[2].fromHex(),
        )
    }.getOrNull()
}
