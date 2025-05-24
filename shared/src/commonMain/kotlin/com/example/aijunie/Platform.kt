package com.example.aijunie

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform