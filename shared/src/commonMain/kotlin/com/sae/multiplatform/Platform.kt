package com.sae.multiplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform