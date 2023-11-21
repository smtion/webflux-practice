package com.example.config.kms

import com.example.config.ObjectMapperConfig.Companion.objectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.scheduler.Schedulers

@Configuration
class KmsConfig {

    @Bean
    fun getWKmsAccount(): KmsAccount =
        loadKmsFile(::kmsMapper)
            .publishOn(Schedulers.single())
            .block()!!

    private fun kmsMapper(jsonString: String): KmsAccount =
        objectMapper.readValue(jsonString, KmsAccount::class.java)
}
