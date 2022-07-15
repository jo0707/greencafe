package com.jo.greencafe.model.entity

import nl.jiankai.annotations.Column

data class Pegawai(
    @Column(name = "id_pegawai")
    var id: Int = 0,

    var namaPegawai: String = "",
    var jenkel: String = "",
    var alamat: String = "",
    var telepon: String = "",
    var jabatan: String = ""
)