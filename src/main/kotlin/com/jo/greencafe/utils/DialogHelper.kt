package com.jo.greencafe.utils

import javax.swing.JOptionPane

object DialogHelper {
    fun showErrorMessage(message: String, title: String? = null) {
        JOptionPane.showMessageDialog(null, message, title ?: "", JOptionPane.WARNING_MESSAGE)
    }

}