package com.example.sample

import com.example.config.logger
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

class SchedulerKind {
    @Test
    fun im() {
        Flux.range(1, 3)
            .publishOn(Schedulers.single())
            .map { it * 2 } // runs on single
            .subscribe { logger.info { it } }

        Thread.sleep(1000)
    }
}