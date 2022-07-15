package com.jo.greencafe.model.entity

import nl.jiankai.annotations.Convert
import java.time.LocalDateTime

data class Transaksi(
    var idTransaksi: Int = 0,
    var idPegawai: Int = 0,
    var noTransaksi: String = "",
    var totalTransaksi: Int = 0,
    var totalTunai: Int = 0,
    var idMeja: Int = 0,

    @Convert(converter = LocalDateTime::class)
    var tglTransaksi: String = ""
)