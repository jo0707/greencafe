package com.jo.greencafe.utils

import javafx.util.StringConverter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object DateHelper {
    private var dbFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private var displayFormat = SimpleDateFormat("EEEE, dd MMMM yyyy - HH:mm:ss", Locale.ROOT)
    private var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    private var fileFormat = SimpleDateFormat("yyyyMMddHHmmss")
    var dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val stringConverter = object : StringConverter<LocalDate?>() {
        override fun toString(date: LocalDate?) = getSimpleDate(date) ?: ""

        override fun fromString(string: String?): LocalDate? {
            return if (!string.isNullOrBlank()) LocalDate.parse(string, dateFormat)
            else null
        }
    }

    fun getDateTime(): String = dbFormat.format(System.currentTimeMillis())
    fun getDateTime(time: Long): String = dbFormat.format(time)
    fun getSimpleDate(date: LocalDate?) = if (date == null) null else simpleDateFormat.format(java.sql.Date.valueOf(date))
    fun getDateTime(date: LocalDateTime): String = dbFormat.format(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()))

    fun getFileDate(): String = fileFormat.format(System.currentTimeMillis())

    fun getDisplayDate(): String = displayFormat.format(System.currentTimeMillis())

    fun dateTimeIsToday(date: String?): Boolean {
        return try {
            val ld = dbFormat.parse(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val now = LocalDate.now()
            ld.dayOfMonth == now.dayOfMonth && ld.monthValue == now.monthValue && ld.year == now.year
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun dateTimeIsThisMonth(date: String?): Boolean {
        return try {
            val ld = dbFormat.parse(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val now = LocalDate.now()
            ld.monthValue == now.monthValue && ld.year == now.year
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}