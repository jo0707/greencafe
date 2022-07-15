package com.jo.greencafe.model.repository

import com.jo.greencafe.model.db.DbHelper.executeNonQuery
import com.jo.greencafe.model.db.DbHelper.executeNonQueryThrows
import com.jo.greencafe.model.db.DbHelper.executeQuery
import com.jo.greencafe.model.db.LocalDateTimeToStringConverter
import com.jo.greencafe.model.db.TimestampToStringConverter
import com.jo.greencafe.model.entity.*
import com.jo.greencafe.utils.DialogHelper.showErrorMessage
import nl.jiankai.mapper.ResultSetMapper
import nl.jiankai.mapper.strategies.LowerCaseUnderscoreFieldNamingStrategy


class DbRepository : IDbRepository {
    companion object {
        private lateinit var repository: DbRepository

        fun getRepository(): DbRepository {
            return if (this::repository.isInitialized) {
                repository
            } else {
                repository = DbRepository()
                repository.mapper.registerAttributeConverter(TimestampToStringConverter())
                repository.mapper.registerAttributeConverter(LocalDateTimeToStringConverter())
                repository
            }
        }
    }

    var mapper = ResultSetMapper(LowerCaseUnderscoreFieldNamingStrategy())
    lateinit var loggedPegawai: Pegawai


    /** LOGIN **/
    override fun getAllLogin(): List<Login> {
        try {
            val query = "SELECT * FROM login"
            return mapper.map(executeQuery(query), Login::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorMessage("Gagal mengambil semua kredensial login :\n\n${e.message}")
        }

        return listOf()
    }

    override fun getAllCustomLogin(): List<CustomLogin> {
        try {
            val query = "SELECT login.*, pegawai.nama_pegawai, pegawai.jabatan FROM login JOIN pegawai ON login.id_pegawai = pegawai.id_pegawai"
            return mapper.map(executeQuery(query), CustomLogin::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorMessage("Gagal mengambil semua kredensial login :\n\n${e.message}")
        }

        return listOf()
    }

    override fun getLogin(username: String, password: String): Login? {
        try {
            val query = "SELECT * FROM login WHERE username = '$username' AND password = '$password'"
            val listLogin = mapper.map(executeQuery(query), Login::class.java)

            return if (listLogin.isNotEmpty()) listLogin[0] else null
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorMessage("Gagal mengambil data login :\n\n${e.message}")
        }

        return null
    }

    override fun insertLogin(login: Login) {
        val queryLogin = "INSERT INTO login VALUES(null, ${login.idPegawai}, '${login.username}', '${login.password}')"
        executeNonQuery(queryLogin)

        repository.insertLog("Penambahan akun pegawai " + login.username)
    }

    override fun updateLogin(login: Login) {
        val queryLogin = "UPDATE login SET username = '${login.username}', password = '${login.password}' WHERE id_pegawai = ${login.idPegawai}"
        executeNonQuery(queryLogin)
    }

    override fun deleteLogin(idLogin: Int) {
        val queryLogin = "DELETE FROM login WHERE id_login = $idLogin"
        executeNonQuery(queryLogin)
    }

    override fun deleteLoginByPegawai(idPegawai: Int) {
        val queryLogin = "DELETE FROM login WHERE id_pegawai = $idPegawai"
        executeNonQuery(queryLogin)
    }

    
    /** PEGAWAI **/
    override fun getPegawai(idPegawai: Int): Pegawai? {
        try {
            val query = "SELECT * FROM pegawai WHERE id_pegawai = $idPegawai"
            val listPegawai = mapper.map(executeQuery(query), Pegawai::class.java)
            return if (listPegawai.isNotEmpty()) listPegawai[0] else null
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorMessage("Gagal mencari data pegawai (id : $idPegawai):\n\n${e.message}")
        }
        
        return null
    }

    override fun getPegawai(namaPegawai: String): Pegawai? {
        try {
            val query = "SELECT * FROM pegawai WHERE nama_pegawai = '$namaPegawai'"
            val listPegawai = mapper.map(executeQuery(query), Pegawai::class.java)
            return if (listPegawai.isNotEmpty()) listPegawai[0] else null
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorMessage("Gagal mencari data pegawai ($namaPegawai) :\n\n${e.message}")
        }
        return null
    }

    override fun getAllPegawai(): List<Pegawai> {
            try {
                val query = "SELECT * FROM pegawai"
                return mapper.map(executeQuery(query), Pegawai::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                showErrorMessage("Gagal mengambil semua pegawai :\n\n${e.message}")
            }
        
            return listOf()
        }

    override fun insertPegawai(pegawai: Pegawai) {
        val query = "INSERT INTO pegawai VALUES(null, '${pegawai.namaPegawai}', '${pegawai.jenkel}', '${pegawai.alamat}', '${pegawai.telepon}', '${pegawai.jabatan}')"
        executeNonQuery(query)
    }

    override fun updatePegawai(pegawai: Pegawai) {
        val query = "UPDATE pegawai SET " +
                "nama_pegawai = '${pegawai.namaPegawai}', " +
                "jenkel = '${pegawai.jenkel}', " +
                "alamat = '${pegawai.alamat}', " +
                "telepon = '${pegawai.telepon}', " +
                "jabatan = '${pegawai.jabatan}' " +
                "WHERE id_pegawai = ${pegawai.id}"

        executeNonQuery(query)
    }

    override fun deletePegawai(idPegawai: Int) {
        val query = "DELETE FROM pegawai WHERE id_pegawai = $idPegawai"
        executeNonQuery(query)
    }
    /** TRANKSAKSI **/
    override fun getAllTransaksi(date1: String?, date2: String?, idPegawai: Int?): List<TransaksiDisplay> {
        try {
            val queryBuilder = StringBuilder("SELECT transaksi.*, pegawai.nama_pegawai, meja.id_meja, meja.no_meja FROM transaksi "
                    + "JOIN pegawai ON transaksi.id_pegawai = pegawai.id_pegawai "
                    + "JOIN meja ON transaksi.id_meja = meja.id_meja ")

            var dateSet = false
            when {
                date1 == null && date2 == null -> {}
                date1 != null && date2 != null -> {
                    queryBuilder.append("WHERE transaksi.tgl_transaksi BETWEEN '${date1} 00:00:00' AND '${date2} 23:59:59' ")
                    dateSet = true
                }
                else -> {
                    val date = date1 ?: date2
                    queryBuilder.append("WHERE transaksi.tgl_transaksi BETWEEN '${date} 00:00:00' AND '${date} 23:59:59' ")
                    dateSet = true
                }
            }

            if (idPegawai != null)
                queryBuilder.append("${if (dateSet) "AND" else "WHERE"} transaksi.id_pegawai = $idPegawai ")

            queryBuilder.append("ORDER BY transaksi.tgl_transaksi DESC")
            return mapper.map(executeQuery(queryBuilder.toString()), TransaksiDisplay::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorMessage("Gagal mengambil riwayat transaksi : \n\n${e.message}")
        }

        return listOf()
    }

    override fun getLastNoTransaksi(): String? {
        try {
            val rs = executeQuery("SELECT transaksi.no_transaksi FROM transaksi ORDER BY id_transaksi DESC LIMIT 1")

            return if (rs?.next() == true) {
                rs.getString("no_transaksi")
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorMessage("Gagal mengambil semua transaksi :\n\n${e.message}")
        }

        return null
    }

    override fun insertTransaksi(transaksi: Transaksi) {
        with(transaksi) {
            val query = "INSERT INTO transaksi VALUES(null, $idPegawai, '$noTransaksi', $totalTransaksi, $totalTunai, $idMeja, '$tglTransaksi')"
            executeNonQuery(query)
        }
    }

    override fun insertTransaksi(transaksi: Transaksi, listOrder: List<Order>) {
        with(transaksi) {
            val query = "INSERT INTO transaksi VALUES(null, $idPegawai, '$noTransaksi', $totalTransaksi, $totalTunai, $idMeja, '$tglTransaksi')"
            executeNonQuery(query)
        }

        listOrder.forEach { insertOrder(it) }
    }


    /** ORDER **/
    override fun insertOrder(order: Order) {
        with(order) {
            val query = "INSERT INTO `order` VALUES(null, $idMenu, '$noTransaksi', $jumlah, $subtotal)"
            executeNonQuery(query)
        }
    }

    override fun getOrder(noTransaksi: String): List<OrderDisplay> {
        try {
            val query = ("SELECT `order`.*, menu.nama_menu, menu.harga FROM `order` JOIN menu ON `order`.id_menu = menu.id_menu WHERE no_transaksi = '$noTransaksi'")
            return mapper.map(executeQuery(query), OrderDisplay::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorMessage("Gagal mengambil menu dari $noTransaksi :\n\n${e.message}")
        }

        return listOf()
    }


    /** MEJA **/
    override fun getAllMeja(): List<Meja> {
        try {
            val query = "SELECT * FROM meja ORDER BY status_meja ASC"
            return mapper.map(executeQuery(query), Meja::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorMessage("Gagal mengambil semua meja :\n\n${e.message}")
        }

        return listOf()
    }

    override fun updateMeja(meja: Meja) {
        val query = "UPDATE meja SET status_meja = '${meja.statusMeja}' WHERE id_meja = ${meja.idMeja}"
        executeNonQuery(query)
    }

    override fun addMeja(noMeja: String) {
        val query = "INSERT INTO meja VALUES(null, '$noMeja', 'TERSEDIA')"
        executeNonQuery(query)
    }

    @Throws
    override fun deleteMeja(id: Int) {
        val query = "DELETE FROM meja where meja.id_meja = $id"
        executeNonQueryThrows(query)
    }


    /** MENU **/
    override fun getAllMenu(): List<Menu> {
        try {
            val query = "SELECT * FROM menu"
            return mapper.map(executeQuery(query), Menu::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorMessage("Gagal mengambil semua menu :\n\n${e.message}")
        }

        return listOf()
    }

    override fun getMenu(idMenu: Int): Menu? {
        try {
            val query = "SELECT * FROM menu WHERE id_menu = $idMenu"
            val listMenu = mapper.map(executeQuery(query), Menu::class.java)
            return if (listMenu.isNotEmpty()) listMenu[0] else null
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorMessage("Gagal mengambil menu (id : $idMenu) :\n\n${e.message}")
        }

        return null
    }

    override fun insertMenu(menu: Menu) {
        with(menu) {
            val query = "INSERT INTO menu VALUES(null, '$namaMenu', '$jenisMenu', $harga)"
            executeNonQuery(query)
        }
    }

    override fun updateMenu(menu: Menu) {
        val query = "UPDATE menu SET nama_menu = '${menu.namaMenu}', jenis_menu = '${menu.jenisMenu}', harga = ${menu.harga} WHERE id_menu = ${menu.idMenu}"
        executeNonQuery(query)
    }

    override fun deleteMenu(id: Int) {
        val query = "DELETE FROM menu WHERE id_menu = $id"
        executeNonQuery(query)
    }


    /** LOG **/
    override fun insertLog(message: String) {
        executeNonQuery("INSERT INTO log (id_pegawai, aktivitas) VALUES (${loggedPegawai.id}, '$message')")
    }

    override fun getAllLog(date1: String?, date2: String?, name: String?): List<Log> {
        try {
            val queryBuilder = StringBuilder("SELECT log.*, pegawai.nama_pegawai, pegawai.jabatan FROM log JOIN pegawai ON log.id_pegawai = pegawai.id_pegawai ")

            var dateSet = false
            when {
                date1 == null && date2 == null -> {}
                date1 != null && date2 != null -> {
                    queryBuilder.append("WHERE log.tanggal BETWEEN '${date1} 00:00:00' AND '${date2} 23:59:59' ")
                    dateSet = true
                }
                else -> {
                    val date = date1 ?: date2
                    queryBuilder.append("WHERE log.tanggal BETWEEN '${date} 00:00:00' AND '${date} 23:59:59' ")
                    dateSet = true
                }
            }

            if (name != null)
                queryBuilder.append("${if (dateSet) "AND" else "WHERE"} pegawai.nama_pegawai = '$name' ")

            queryBuilder.append("ORDER BY log.tanggal DESC")
            return mapper.map(executeQuery(queryBuilder.toString()), Log::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorMessage("Gagal mengambil riwayat aktivitas : \n\n${e.message}")
        }

        return listOf()
    }



    override fun isAccountExists(username: String, name: String): Boolean {
        try {
            val rss = executeQuery("SELECT is_account_exists('${username}', '${name}')")

            while (rss!!.next()) {
                return rss.getInt(1) != 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorMessage("Gagal mengecek ketersediaan pegawai : \n\n${e.message}")
        }
        return false
    }
}