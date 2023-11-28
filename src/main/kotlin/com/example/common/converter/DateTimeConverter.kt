package com.example.common.converter

import com.example.common.constant.LOCAL_DATE_TIME_FORMATTER
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import java.time.LocalDateTime

@ReadingConverter
class StringToLocalDateTimeConverter: Converter<String, LocalDateTime> {
    override fun convert(source: String): LocalDateTime =
        LocalDateTime.parse(source, LOCAL_DATE_TIME_FORMATTER)
}

@WritingConverter
class LocalDateTimeToStringConverter: Converter<LocalDateTime, String> {
    override fun convert(source: LocalDateTime): String =
        source.format(LOCAL_DATE_TIME_FORMATTER)
}