package com.jo.greencafe.controller.utils.listener

import com.jo.greencafe.model.entity.Pegawai

interface PegawaiListener {
    fun onComplete(pegawai: Pegawai? = null)
}