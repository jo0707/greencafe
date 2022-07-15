package com.jo.greencafe.model.db

import com.jo.greencafe.utils.DateHelper
import nl.jiankai.annotations.Converter
import nl.jiankai.mapper.converters.AttributeConverter
import java.sql.Timestamp


@Converter(autoApply = true)
class TimestampToStringConverter : AttributeConverter<Timestamp, String> {
    override fun convert(value: Timestamp): String {
        return DateHelper.getDateTime(value.time)
    }

    override fun source(): Class<Timestamp> {
        return Timestamp::class.java
    }

    override fun target(): Class<String> {
        return String::class.java
    }
}