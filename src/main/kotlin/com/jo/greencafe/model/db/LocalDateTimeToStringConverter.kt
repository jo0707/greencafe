package com.jo.greencafe.model.db

import com.jo.greencafe.utils.DateHelper
import nl.jiankai.annotations.Converter
import nl.jiankai.mapper.converters.AttributeConverter
import java.time.LocalDateTime


@Converter(autoApply = true)
class LocalDateTimeToStringConverter : AttributeConverter<LocalDateTime, String> {
    override fun convert(value: LocalDateTime): String {
        return DateHelper.getDateTime(value)
    }

    override fun source(): Class<LocalDateTime> {
        return LocalDateTime::class.java
    }

    override fun target(): Class<String> {
        return String::class.java
    }
}