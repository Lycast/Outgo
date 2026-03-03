package fr.abknative.outgo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform