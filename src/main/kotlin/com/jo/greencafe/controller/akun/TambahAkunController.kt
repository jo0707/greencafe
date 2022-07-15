package com.jo.greencafe.controller.akun

import com.jo.greencafe.controller.utils.Const
import com.jo.greencafe.controller.utils.listener.LoginListener
import com.jo.greencafe.model.entity.CustomLogin
import com.jo.greencafe.model.entity.Login
import com.jo.greencafe.model.entity.Pegawai
import com.jo.greencafe.model.repository.DbRepository
import io.github.palexdev.materialfx.controls.MFXPasswordField
import io.github.palexdev.materialfx.controls.MFXTextField
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.*


class TambahAkunController: Initializable {
    private lateinit var loginListener: LoginListener
    private lateinit var login: CustomLogin
    private lateinit var listPegawai: List<Pegawai>

    private var mode = ""
    private val repository = DbRepository.getRepository()

    @FXML
    private lateinit var errorLb: Label

    @FXML
    private lateinit var namaPegawaiCb: MFXLegacyComboBox<Any>

    @FXML
    private lateinit var passwordTf: MFXPasswordField

    @FXML
    private lateinit var titleLb: Label

    @FXML
    private lateinit var usernameTf: MFXTextField

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        listPegawai = repository.getAllPegawai()
        namaPegawaiCb.items.addAll(*listPegawai.map { it.namaPegawai }.toTypedArray())
    }

    @FXML
    fun onDoneBtnClicked(event: ActionEvent) {
        val name = namaPegawaiCb.value.toString()
        val username = usernameTf.text
        val password = passwordTf.text

        errorLb.text = when {
            name.isBlank() -> "Nama kosong, coba lagi"
            username.isBlank() -> "Username kosong, coba lagi"
            password.isBlank() -> "Password kosong, coba lagi"
            else -> ""
        }

        if (errorLb.text.isNotBlank()) return

        if (mode == Const.CREATE) {
            create(Login(0, listPegawai.find { it.namaPegawai == name }!!.id, username, password))
        } else {
            update(Login(0, listPegawai.find { it.namaPegawai == name }!!.id, username, password))
        }

        loginListener.onComplete()
    }

    private fun create(login: Login) {
        repository.insertLogin(login)
        repository.insertLog("Penambahaan akun ${login.username}")
    }

    private fun update(login: Login) {
        repository.updateLogin(login)
        repository.insertLog("pembaruan akun ${login.username}")
    }

    fun set(loginListener: LoginListener, login: CustomLogin?, mode: String) {
        var title = "Tambah Akun"

        if (mode == Const.UPDATE) {
            title = "Edit Akun"
            this.login = login!!

            namaPegawaiCb.value = login.namaPegawai
            usernameTf.text = login.username
            passwordTf.text = login.password
        }

        titleLb.text = title
        this.mode = mode
        this.loginListener = loginListener
    }
}