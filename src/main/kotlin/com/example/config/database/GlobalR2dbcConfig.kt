package com.example.config.database

import com.example.common.converter.CodeEnumReadingConverterFactory
import com.example.common.converter.CodeEnumWritingConverterFactory
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration

abstract class GlobalR2dbcConfig : AbstractR2dbcConfiguration() {
    override fun getCustomConverters() =
        listOf(
            CodeEnumReadingConverterFactory(),
            CodeEnumWritingConverterFactory(),
        )
}
