package com.jo.greencafe.model.entity

import nl.jiankai.annotations.Column

data class Meja(
    var idMeja: Int = 0,
    @Column(name = "no_meja")
    var noMeja: String = "",
    var statusMeja: String = ""
)
