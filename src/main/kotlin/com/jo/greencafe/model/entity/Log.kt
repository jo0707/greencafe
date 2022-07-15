package com.jo.greencafe.model.entity

import com.jo.greencafe.model.db.TimestampToStringConverter
import nl.jiankai.annotations.Convert

data class Log(
    var idLog: Int = 0,
    var idPegawai: Int = 0,
    var namaPegawai: String = "",
    var jabatan: String = "",
    var aktivitas: String = "",

    @Convert(converter = TimestampToStringConverter::class)
    var tanggal: String = ""
)