package com.jo.greencafe.model.entity

import com.jo.greencafe.model.db.LocalDateTimeToStringConverter
import nl.jiankai.annotations.Convert

data class TransaksiDisplay(
    var idTransaksi: Int = 0,
    var idPegawai: Int = 0,
    var namaPegawai: String = "",
    var noTransaksi: String = "",
    var totalTransaksi: Int = 0,
    var totalTunai: Int = 0,
    var idMeja: Int = 0,
    var noMeja: String = "",

    @Convert(converter = LocalDateTimeToStringConverter::class)
    var tglTransaksi: String = ""
) {
    fun toTransaksi() = Transaksi(idTransaksi, idPegawai, noTransaksi, totalTransaksi, totalTunai, idMeja, tglTransaksi)
}