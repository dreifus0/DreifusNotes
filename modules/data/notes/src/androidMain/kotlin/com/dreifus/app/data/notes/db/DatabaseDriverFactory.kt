package com.dreifus.app.data.notes.db

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import java.security.SecureRandom

actual class DatabaseDriverFactory(private val context: Context) {

    actual fun create(): SqlDriver {
        System.loadLibrary("sqlcipher")
        val passphrase = getOrCreatePassphrase()
        val factory = SupportOpenHelperFactory(passphrase)
        return AndroidSqliteDriver(
            schema = NotesDatabase.Schema,
            context = context,
            name = "notes.db",
            factory = factory,
        )
    }

    private fun getOrCreatePassphrase(): ByteArray {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val prefs = EncryptedSharedPreferences.create(
            context,
            "notes_db_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
        val key = "db_passphrase"
        val existing = prefs.getString(key, null)
        if (existing != null) return android.util.Base64.decode(
            existing,
            android.util.Base64.DEFAULT
        )

        val passphrase = ByteArray(32).also { SecureRandom().nextBytes(it) }
        prefs.edit().putString(
            key,
            android.util.Base64.encodeToString(passphrase, android.util.Base64.DEFAULT)
        ).apply()
        return passphrase
    }
}
