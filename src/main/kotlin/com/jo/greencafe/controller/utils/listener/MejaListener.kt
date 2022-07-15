package com.jo.greencafe.controller.utils.listener

import com.jo.greencafe.model.entity.Meja

interface MejaListener {
    fun onComplete(meja: Meja)
}