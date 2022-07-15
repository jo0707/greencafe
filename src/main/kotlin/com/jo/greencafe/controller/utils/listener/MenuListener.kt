package com.jo.greencafe.controller.utils.listener

import com.jo.greencafe.model.entity.Menu

interface MenuListener {
    fun onComplete(menu: Menu?)
}