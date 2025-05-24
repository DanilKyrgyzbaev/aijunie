package com.example.aijunie.security

import com.soywiz.krypto.SecureRandom
import com.soywiz.krypto.encoding.Base64
import com.soywiz.krypto.encoding.hex
import com.soywiz.krypto.sha256
import kotlin.experimental.xor

class PasswordService {
    companion object {
        private const val SALT_LENGTH = 16
        private const val ITERATIONS = 10000
    }

    fun hashPassword(password: String): String {
        val salt = SecureRandom.nextBytes(SALT_LENGTH)
        val hash = pbkdf2(password, salt)

        // Format: iterations:salt:hash
        return "$ITERATIONS:${salt.hex}:${hash.hex}"
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        try {
            val parts = hashedPassword.split(":")
            if (parts.size != 3) return false

            val iterations = parts[0].toInt()
            val salt = parts[1].hexToByteArray()
            val storedHash = parts[2].hexToByteArray()

            val computedHash = pbkdf2(password, salt, iterations)

            return storedHash.contentEquals(computedHash)
        } catch (e: Exception) {
            return false
        }
    }

    private fun pbkdf2(password: String, salt: ByteArray, iterations: Int = ITERATIONS): ByteArray {
        var result = password.toByteArray().sha256().bytes
        val saltedPassword = result + salt

        repeat(iterations) {
            result = saltedPassword.sha256().bytes
        }

        return result
    }

    private fun String.hexToByteArray(): ByteArray {
        return chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
    }
}
