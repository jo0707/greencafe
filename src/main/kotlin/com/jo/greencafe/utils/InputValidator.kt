package com.jo.greencafe.utils

object InputValidator {
    fun isPhoneValid(input: String) = !input.contains("[^\\d+ ()-]".toRegex())
}