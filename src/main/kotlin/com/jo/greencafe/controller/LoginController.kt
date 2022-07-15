package com.jo.greencafe.controller

import com.jo.greencafe.controller.utils.Const
import com.jo.greencafe.model.repository.DbRepository
import com.jo.greencafe.utils.ResHelper
import io.github.palexdev.materialfx.controls.MFXButton
import io.github.palexdev.materialfx.controls.MFXPasswordField
import io.github.palexdev.materialfx.controls.MFXTextField
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Label
import javax.swing.JOptionPane


class LoginController {
    val repository = DbRepository.getRepository()
    private var mainScene: Scene? = null

    @FXML
    private lateinit var errorLb: Label

    @FXML
    private lateinit var loginBtn: MFXButton

    @FXML
    private lateinit var passwordTf: MFXPasswordField

    @FXML
    private lateinit var usernameTf: MFXTextField

    @FXML
    fun loginBtnActionPerformed() {
        checkLogin()
    }

    private fun checkLogin() {
        errorLb.text = null
        val username = usernameTf.text.trim()
        val password = passwordTf.text.trim()

        errorLb.text = when {
            username.isBlank() -> "Username kosong"
            password.isBlank() -> "Password kosong"
            else -> null
        }

        if (errorLb.text != null) return

        val login = repository.getLogin(username, password)
        if (login != null) {
            bukaHalamanUtama(login.idPegawai)
        } else {
            println("A")
            errorLb.text = "Username / password salah"
        }
    }

    private fun bukaHalamanUtama(idPegawai: Int) {
        val pegawai = repository.getPegawai(idPegawai)

        if (pegawai != null) {
            repository.loggedPegawai = pegawai
            var title = ""
            mainScene = when (pegawai.jabatan) {
                "ADMIN" -> {
                    title = Const.getTitle("Admin")
                    Scene(ResHelper.loadFXML("admin.fxml").load())
                }
                "MANAGER" -> {
                    title = Const.getTitle("Manager")
                   Scene(ResHelper.loadFXML("manager.fxml").load())
                }
                "KASIR" -> {
                    title = Const.getTitle("Kasir")
                    Scene(ResHelper.loadFXML("kasir.fxml").load())
                }
                else -> null
            }

            repository.insertLog("${pegawai.namaPegawai} melakukan login (id : ${pegawai.id})")

            if (mainScene != null) {
                val stage = ResHelper.getStage(loginBtn)
                stage.title = title
                stage.scene = mainScene
            }
        } else {
            JOptionPane.showMessageDialog(
                null,
                String.format("Pegawai dengan id $idPegawai tidak ditemukan."),
                "Kesalahan",
                JOptionPane.ERROR_MESSAGE
            )
        }
    }
}
