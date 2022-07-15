package com.jo.greencafe.controller.utils

object Const {
    // css id keys
    const val PRIMARY = "primary"
    const val WARNING = "warning"
    const val NEGATIVE  = "negative"
    const val DISABLED = "disabled"

    // editor mode keys
    const val CREATE = "create"
    const val UPDATE = "update"

    fun getTitle(name: String) = "GreenCafe - $name"
}