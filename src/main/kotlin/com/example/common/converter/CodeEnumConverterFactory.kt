package com.example.common.converter

import com.example.common.enumtype.CodeEnum
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory

class CodeEnumReadingConverterFactory : ConverterFactory<String, CodeEnum> {
    private val converterMap = HashMap<Any, Converter<String, out CodeEnum>>()

    override fun <T : CodeEnum> getConverter(targetType: Class<T>): Converter<String, T> {
        if (! targetType.isEnum) {
            throw ClassCastException("$targetType is not implemented as an enum class.")
        }

        if (! converterMap.containsKey(targetType)) {
            converterMap[targetType] = CodeEnumReadingConverter(targetType)
        }

        @Suppress("UNCHECKED_CAST")
        return converterMap[targetType] as Converter<String, T>
    }

    inner class CodeEnumReadingConverter<T : CodeEnum>(type: Class<T>) : Converter<String, T> {
        private val enumConstantsMap = type.enumConstants.associateBy { it.code }

        override fun convert(source: String): T {
            if (source.isBlank()) {
                throw IllegalArgumentException("Source cannot be an empty or blank.")
            }

            return enumConstantsMap[source] ?: throw NoSuchElementException("Enum not found from value [$source]")
        }
    }
}

class CodeEnumWritingConverterFactory : ConverterFactory<CodeEnum, CharSequence> {
    override fun <T : CharSequence> getConverter(targetType: Class<T>): Converter<CodeEnum, T> {
        return CodeEnumWritingConverter()
    }

    inner class CodeEnumWritingConverter<T : CharSequence> : Converter<CodeEnum, T> {
        override fun convert(codeEnum: CodeEnum): T {
            @Suppress("UNCHECKED_CAST")
            return codeEnum.code as T
        }
    }
}