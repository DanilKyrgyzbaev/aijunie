package com.example.aijunie.security

import com.soywiz.krypto.encoding.Base64
import com.soywiz.krypto.sha256
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.json.*
import java.util.UUID
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

class JwtConfig(private val secret: String) {
    companion object {
        private const val ISSUER = "ktor-auth-server"
        private const val AUDIENCE = "ktor-auth-audience"
        private val ACCESS_TOKEN_EXPIRATION = 15.minutes
        private val REFRESH_TOKEN_EXPIRATION = 7.days
    }

    // Create JWT token
    private fun createToken(userId: UUID, expiration: Instant): String {
        // Create header
        val header = buildJsonObject {
            put("alg", JsonPrimitive("HS256"))
            put("typ", JsonPrimitive("JWT"))
        }

        // Create payload
        val payload = buildJsonObject {
            put("iss", JsonPrimitive(ISSUER))
            put("aud", JsonPrimitive(AUDIENCE))
            put("userId", JsonPrimitive(userId.toString()))
            put("exp", JsonPrimitive(expiration.epochSeconds))
            put("iat", JsonPrimitive(Clock.System.now().epochSeconds))
        }

        // Encode header and payload
        val headerBase64 = Base64.encode(header.toString().encodeToByteArray(), url = true)
        val payloadBase64 = Base64.encode(payload.toString().encodeToByteArray(), url = true)

        // Create signature
        val dataToSign = "$headerBase64.$payloadBase64"
        val signature = createSignature(dataToSign)

        // Return complete token
        return "$headerBase64.$payloadBase64.$signature"
    }

    private fun createSignature(data: String): String {
        val signatureBytes = (data + secret).encodeToByteArray().sha256().bytes
        return Base64.encode(signatureBytes, url = true)
    }

    fun createAccessToken(userId: UUID): String {
        val expiration = Clock.System.now() + ACCESS_TOKEN_EXPIRATION
        return createToken(userId, expiration)
    }

    fun createRefreshToken(userId: UUID): String {
        val expiration = Clock.System.now() + REFRESH_TOKEN_EXPIRATION
        return createToken(userId, expiration)
    }

    fun verifyToken(token: String): UUID? {
        try {
            // Split token into parts
            val parts = token.split(".")
            if (parts.size != 3) return null

            val headerBase64 = parts[0]
            val payloadBase64 = parts[1]
            val signatureProvided = parts[2]

            // Verify signature
            val dataToSign = "$headerBase64.$payloadBase64"
            val signatureCalculated = createSignature(dataToSign)

            if (signatureProvided != signatureCalculated) return null

            // Decode payload
            val payloadJson = Json.parseToJsonElement(
                Base64.decode(payloadBase64, url = true).decodeToString()
            ).jsonObject

            // Verify expiration
            val expiration = payloadJson["exp"]?.jsonPrimitive?.longOrNull
                ?: return null

            if (Instant.fromEpochSeconds(expiration) < Clock.System.now()) return null

            // Verify issuer and audience
            val issuer = payloadJson["iss"]?.jsonPrimitive?.contentOrNull
            val audience = payloadJson["aud"]?.jsonPrimitive?.contentOrNull

            if (issuer != ISSUER || audience != AUDIENCE) return null

            // Extract user ID
            val userId = payloadJson["userId"]?.jsonPrimitive?.contentOrNull
                ?: return null

            return UUID.fromString(userId)
        } catch (e: Exception) {
            return null
        }
    }
}
