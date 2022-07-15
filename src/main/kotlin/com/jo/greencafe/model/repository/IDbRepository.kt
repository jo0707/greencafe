package com.jo.greencafe.model.repository

import com.jo.greencafe.model.entity.*

interface IDbRepository {
    fun getAllLogin(): List<Login>
    fun getAllCustomLogin(): List<CustomLogin>
    fun getLogin(username: String, password: String): Login?
    fun insertLogin(login: Login)
    fun updateLogin(login: Login)
    fun deleteLoginByPegawai(idPegawai: Int)
    fun deleteLogin(idLogin: Int)

    fun getPegawai(idPegawai: Int): Pegawai?
    fun getPegawai(namaPegawai: String): Pegawai?
    fun getAllPegawai(): List<Pegawai>
    fun insertPegawai(pegawai: Pegawai)
    fun updatePegawai(pegawai: Pegawai)
    fun deletePegawai(idPegawai: Int)

    fun getAllTransaksi(date1: String? = null, date2: String? = null, idPegawai: Int? = null): List<TransaksiDisplay>
    fun getLastNoTransaksi(): String?
    fun insertTransaksi(transaksi: Transaksi)
    fun insertTransaksi(transaksi: Transaksi, listOrder: List<Order>)

    fun insertOrder(order: Order)
    fun getOrder(noTransaksi: String): List<OrderDisplay>

    fun getAllMeja(): List<Meja>
    fun updateMeja(meja: Meja)
    fun addMeja(noMeja: String)
    fun deleteMeja(id: Int)

    fun getAllMenu(): List<Menu>
    fun getMenu(idMenu: Int): Menu?
    fun insertMenu(menu: Menu)
    fun updateMenu(menu: Menu)
    fun deleteMenu(id: Int)

    fun getAllLog(date1: String? = null, date2: String? = null, name: String? = null): List<Log>
    fun insertLog(message: String)
    fun isAccountExists(username: String, name: String): Boolean
}