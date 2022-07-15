package com.jo.greencafe.model.entity

data class Login(
    var idLogin: Int = 0,
    var idPegawai: Int = 0,
    var username: String = "",
    var password: String = ""
)