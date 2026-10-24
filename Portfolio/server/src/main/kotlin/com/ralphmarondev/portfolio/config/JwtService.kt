package com.ralphmarondev.portfolio.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService {
    // Set JWT_SECRET (32+ chars) so tokens survive restarts. Falls back to a random key.
    private val key = System.getenv("JWT_SECRET")
        ?.takeIf { it.length >= 32 }
        ?.let { Keys.hmacShaKeyFor(it.toByteArray()) }
        ?: run {
            println("WARNING: JWT_SECRET missing or too short; using a random key (tokens reset on restart).")
            Keys.secretKeyFor(SignatureAlgorithm.HS256)
        }
    private val accessTokenExpirationMs: Long = 1000L * 60 * 60 // 1 hour
    private val refreshTokenExpirationMs: Long = 1000L * 60 * 60 * 24 * 7 // 7 days

    fun generateAccessToken(userId: Long, username: String, role: String): String {
        val now = Date()
        return Jwts.builder()
            .setSubject(userId.toString())
            .claim("username", username)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + accessTokenExpirationMs))
            .signWith(key)
            .compact()
    }

    fun generateRefreshToken(userId: Long, username: String): String {
        val now = Date()
        return Jwts.builder()
            .setSubject(userId.toString())
            .claim("username", username)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + refreshTokenExpirationMs))
            .signWith(key)
            .compact()
    }

    private fun claims(token: String) = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .body

    fun extractUserId(token: String): Long = claims(token).subject.toLong()

    fun extractRole(token: String): String = claims(token).get("role", String::class.java)
}
