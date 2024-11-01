package com.example.kmptemplate

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform