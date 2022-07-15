package com.jo.greencafe.model.entity

data class CustomLogin(
    var idLogin: Int = 0,
    var idPegawai: Int = 0,
    var namaPegawai: String = "",
    var jabatan: String = "",
    var username: String = "",
    var password: String = ""
)