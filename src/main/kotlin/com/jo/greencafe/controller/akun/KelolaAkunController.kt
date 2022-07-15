package com.jo.greencafe.controller.akun

import com.jo.greencafe.controller.utils.listener.LoginListener
import com.jo.greencafe.controller.utils.Const
import com.jo.greencafe.model.entity.CustomLogin
import com.jo.greencafe.model.repository.DbRepository
import com.jo.greencafe.utils.ResHelper
import io.github.palexdev.materialfx.controls.MFXButton
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import java.net.URL
import java.util.*


class KelolaAkunController : Initializable {
    val repository = DbRepository.getRepository()

    @FXML
    private lateinit var akunTable: MFXLegacyTableView<Any>

    @FXML
    private lateinit var editBtn: MFXButton

    @FXML
    private lateinit var hapusBtn: MFXButton

    @FXML
    private lateinit var jabatanColumn: TableColumn<Any, Any>

    @FXML
    private lateinit var namaColumn: TableColumn<Any, Any>

    @FXML
    private lateinit var usernameColumn: TableColumn<Any, Any>


    @FXML
    fun onEditClicked(event: ActionEvent) {
        val login = akunTable.selectionModel.selectedItem as CustomLogin?
        startAddDialog(login, Const.UPDATE)
    }

    @FXML
    fun onHapusClicked(event: ActionEvent) {
        val login = akunTable.selectionModel.selectedItem as CustomLogin?

        if (login != null) {
            if (login.idPegawai == repository.loggedPegawai.id) {
                return
            }

            repository.deleteLogin(login.idLogin)
            repository.insertLog("Penghapusan akun ${login.username}")
            setTableData()
        }
    }

    @FXML
    fun onTambahClicked(event: ActionEvent) {
        startAddDialog(null, Const.CREATE)
    }

    @FXML
    fun onTableClicked(event: MouseEvent) {
        setButton(akunTable.selectionModel.selectedIndex >= 0)
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        setupTable()
        setButton(false)
    }

    private fun startAddDialog(customLogin: CustomLogin?, mode: String) {
        val tambahLoginLoader = ResHelper.loadFXML("tambahLogin.fxml")
        val scene   = Scene(tambahLoginLoader.load())
        val stage   = Stage()

        stage.title = Const.getTitle(if (mode == Const.CREATE) "Tambah Akun" else "Edit Akun")
        stage.scene = scene
        stage.show()

        val controller = tambahLoginLoader.getController<TambahAkunController>()
        val listener = object : LoginListener {
            override fun onComplete(login: CustomLogin?) {
                stage.close()
                setTableData()
            }
        }
        controller.set(listener, customLogin, mode)
    }

    private fun setTableData() {
        akunTable.items.clear()
        akunTable.items.addAll(*repository.getAllCustomLogin().toTypedArray())
    }

    private fun setButton(enabled: Boolean) {
        editBtn.id = if (enabled) Const.WARNING else Const.DISABLED
        hapusBtn.id = if (enabled) Const.NEGATIVE else Const.DISABLED
        editBtn.isDisable = !enabled
        hapusBtn.isDisable = !enabled
    }

    private fun setupTable() {
        namaColumn.cellValueFactory = PropertyValueFactory("namaPegawai")
        usernameColumn.cellValueFactory = PropertyValueFactory("username")
        jabatanColumn.cellValueFactory = PropertyValueFactory("jabatan")
        setTableData()
    }
}
