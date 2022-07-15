package com.jo.greencafe.model.db

import com.jo.greencafe.utils.DialogHelper
import java.sql.Connection
import java.sql.DriverManager.getConnection
import java.sql.Statement

object Koneksi {
    private var con: Connection? = null
    var stmt: Statement? = null

    fun getConnection(): Connection? {
        try {
            val url = "jdbc:mysql://localhost/greencafe"
            val user = "root"
            val pass = ""

            if (con == null) {
                con = getConnection(url, user, pass)
                stmt = con!!.createStatement()
            } else {
                if (con!!.isClosed) {
                    con = getConnection(url, user, pass)
                    stmt = con!!.createStatement()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            DialogHelper.showErrorMessage("Gagal menghubungkan database, cek apakah server database aktif.\n\n${e.message}")
        }

        return con
    }
}