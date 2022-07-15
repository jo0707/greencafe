package com.jo.greencafe.controller.pegawai

import com.jo.greencafe.controller.utils.listener.PegawaiListener
import com.jo.greencafe.controller.utils.Const
import com.jo.greencafe.model.entity.Pegawai
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


class KelolaPegawaiController : Initializable {
    val repository = DbRepository.getRepository()

    @FXML
    private lateinit var editBtn: MFXButton
    @FXML
    private lateinit var hapusBtn: MFXButton
    @FXML
    private lateinit var pegawaiTable: MFXLegacyTableView<Any>

    @FXML
    private lateinit var namaColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var jenkelColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var alamatColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var teleponColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var jabatanColumn: TableColumn<Any, Any>

    fun onExportClicked() {
        val headers = listOf("Nama Pegawai", "Jenis Kelamin", "Alamat", "Telepon", "Jabatan")
        val fields = listOf("namaPegawai", "jenkel", "alamat", "telepon", "jabatan")
        val listPegawai = pegawaiTable.items.toList()

        ResHelper.exportToExcel("GreenCafe_Pegawai", headers, listPegawai, fields)
    }

    @FXML
    fun onEditClicked(event: ActionEvent) {
        val pegawai = pegawaiTable.selectionModel.selectedItem as Pegawai?
        startAddDialog(pegawai, Const.UPDATE)
    }

    @FXML
    fun onHapusClicked(event: ActionEvent) {
        val pegawai = pegawaiTable.selectionModel.selectedItem as Pegawai?

        if (pegawai != null) {
            if (pegawai.id == repository.loggedPegawai.id) {
                return
            }

            repository.deletePegawai(pegawai.id)
            repository.insertLog("Penghapusan pegawai ${pegawai.namaPegawai}")
            setTableData()
        }
    }

    @FXML
    fun onTambahClicked(event: ActionEvent) {
        startAddDialog(null, Const.CREATE)
    }

    @FXML
    fun onTableClicked(event: MouseEvent) {
        setButton(pegawaiTable.selectionModel.selectedIndex >= 0)
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        setupTable()
        setButton(false)
    }

    private fun startAddDialog(pegawai: Pegawai?, mode: String) {
        val tambahPegawaiLoader = ResHelper.loadFXML("tambahPegawai.fxml")
        val scene   = Scene(tambahPegawaiLoader.load())
        val stage   = Stage()

        stage.title = Const.getTitle(if (mode == Const.CREATE) "Tambah Pegawai" else "Edit Pegawai")
        stage.scene = scene
        stage.show()

        val controller = tambahPegawaiLoader.getController<TambahPegawaiController>()
        val listener = object : PegawaiListener {
            override fun onComplete(pegawai: Pegawai?) {
                stage.close()
                setTableData()
            }
        }
        controller.set(listener, pegawai, mode)
    }

    private fun setTableData() {
        pegawaiTable.items.clear()
        pegawaiTable.items.addAll(*repository.getAllPegawai().toTypedArray())
    }

    private fun setButton(enabled: Boolean) {
        editBtn.id = if (enabled) Const.WARNING else Const.DISABLED
        hapusBtn.id = if (enabled) Const.NEGATIVE else Const.DISABLED
        editBtn.isDisable = !enabled
        hapusBtn.isDisable = !enabled
    }

    private fun setupTable() {
        namaColumn.cellValueFactory = PropertyValueFactory("namaPegawai")
        jenkelColumn.cellValueFactory = PropertyValueFactory("jenkel")
        alamatColumn.cellValueFactory = PropertyValueFactory("alamat")
        teleponColumn.cellValueFactory = PropertyValueFactory("telepon")
        jabatanColumn.cellValueFactory = PropertyValueFactory("jabatan")
        setTableData()
    }
}
