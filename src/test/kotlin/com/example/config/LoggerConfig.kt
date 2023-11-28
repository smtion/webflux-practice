package com.example.config

import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.LocalTime

val logger = KotlinLogging.logger {}

fun log(msg: Any) = logger.info { "${LocalTime.now()} [${Thread.currentThread().name}] $msg" }
