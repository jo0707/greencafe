package com.jo.greencafe.model.db

import com.jo.greencafe.model.db.Koneksi.stmt
import com.jo.greencafe.utils.DialogHelper
import java.sql.ResultSet
import java.sql.SQLException


object DbHelper {
    @Throws(SQLException::class)
    fun executeQuery(query: String?): ResultSet? {
        Koneksi.getConnection()
        return stmt!!.executeQuery(query)
    }

    fun executeNonQuery(query: String?): Boolean {
        try {
            Koneksi.getConnection()
            return stmt!!.execute(query)
        } catch (e: Exception) {
            e.printStackTrace()
            DialogHelper.showErrorMessage("Gagal mengeksekusi query :${e.message}")
        }

        return false
    }

    @Throws
    fun executeNonQueryThrows(query: String?): Boolean {
        Koneksi.getConnection()
        return stmt!!.execute(query)
    }
}