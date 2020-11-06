package com.example.bettertogether.utils

import org.koin.core.KoinApplication

fun Any.debug(message: String) {
    KoinApplication.logger.debug("DEBUG $message")
}

fun Any.info(message: String) {
    KoinApplication.logger.info("INFO $message")
}

fun Any.error(message: String) {
    KoinApplication.logger.error("ERROR $message")
}
