package fr.abknative.outgo.server.core.repository

interface TransactionRunner {
    operator fun <T> invoke(block: () -> T): T
}