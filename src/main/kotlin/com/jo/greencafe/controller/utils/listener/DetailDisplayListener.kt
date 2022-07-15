package com.jo.greencafe.controller.utils.listener

import com.jo.greencafe.model.entity.OrderDisplay

interface OrderDisplayListener {
    fun onComplete(order: OrderDisplay)
}