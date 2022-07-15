package com.jo.greencafe.controller.utils.listener

import com.jo.greencafe.model.entity.CustomLogin

interface LoginListener {
    fun onComplete(login: CustomLogin? = null)
}