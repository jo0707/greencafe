package com.jo.greencafe.controller.meja

import com.jo.greencafe.controller.utils.Const
import com.jo.greencafe.controller.utils.listener.MejaListener
import com.jo.greencafe.controller.utils.listener.TambahMejaListener
import com.jo.greencafe.model.entity.Meja
import com.jo.greencafe.model.repository.DbRepository
import com.jo.greencafe.utils.ResHelper
import io.github.palexdev.materialfx.controls.MFXButton
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import java.net.URL
import java.util.*

class KelolaMejaController: Initializable {
    private val repository = DbRepository.getRepository()
    private lateinit var listMeja: List<Meja>
    private lateinit var listener: MejaListener

    @FXML
    private lateinit var noMejaColumn: TableColumn<Any, Any>

    @FXML
    private lateinit var noMejaTable: MFXLegacyTableView<Any>

    @FXML
    private lateinit var pilihMejaBtn: MFXButton

    @FXML
    private lateinit var statusColumn: TableColumn<Any, Any>

    @FXML
    private lateinit var titleLb: Label

    @FXML
    private lateinit var ubahStatusBtn: MFXButton

    @FXML
    fun onPilihMejaBtn(event: ActionEvent) {
        val meja = noMejaTable.selectionModel.selectedItem as Meja?
        if (meja != null) listener.onComplete(meja)
    }

    @FXML
    fun onTambahClicked(event: ActionEvent)  {
        startAddMejaDialog()
    }

    @FXML
    fun onHapusClicked(event: ActionEvent) {
        val meja = noMejaTable.selectionModel.selectedItem as Meja?
        if (meja != null) {
            try {
                repository.deleteMeja(meja.idMeja)
            } catch (e: Exception) {
                com.jo.greencafe.utils.DialogHelper.showErrorMessage("Meja tidak dapat dihapus karena sudah ada transaksi yang menggunakan meja tersebut.")
            }
        }

        setTableData()
    }

    @FXML
    fun onUbahStatusClicked(event: ActionEvent) {
        val meja = noMejaTable.selectionModel.selectedItem as Meja?
        if (meja != null) {
            repository.updateMeja(Meja(meja.idMeja, meja.noMeja, if (meja.statusMeja == "DIGUNAKAN") "TERSEDIA" else "DIGUNAKAN"))
            setTableData()
        }
    }

    @FXML
    fun onTableClicked(event: MouseEvent) {
        val meja = noMejaTable.selectionModel.selectedItem as Meja?

        if (meja != null) {
            if (meja.statusMeja == "DIGUNAKAN") setButtonState(null, false)
            else  setButtonState(null, true)
            setButtonState(true, null)
        }
    }

    fun pick(listener: MejaListener) {
        this.listener = listener
        titleLb.text = "Pilih Meja"
        pilihMejaBtn.isVisible = true
        setButtonState(null, false)
    }

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        noMejaColumn.cellValueFactory = PropertyValueFactory("noMeja")
        statusColumn.cellValueFactory = PropertyValueFactory("statusMeja")
        setTableData()

        pilihMejaBtn.isVisible = false
        setButtonState(enableUbahStatus = false, enablePilihMeja = false)
    }

    private fun setButtonState(enableUbahStatus: Boolean? = null, enablePilihMeja: Boolean? = null) {
        if (enableUbahStatus != null) {
            ubahStatusBtn.isDisable = !enableUbahStatus
            ubahStatusBtn.id = if (enableUbahStatus) Const.PRIMARY else Const.DISABLED

            //hapusMejaBtns.isDisable = !enableUbahStatus
            //hapusMejaBtns.id = if (enableUbahStatus) Const.NEGATIVE else Const.DISABLED
        }

        if (enablePilihMeja != null) {
            pilihMejaBtn.isDisable = !enablePilihMeja
            pilihMejaBtn.id = if (enablePilihMeja) Const.PRIMARY else Const.DISABLED
        }
    }

    private fun setTableData() {
        listMeja = repository.getAllMeja()

        noMejaTable.items.clear()
        noMejaTable.items.addAll(*listMeja.toTypedArray())
    }

    private fun startAddMejaDialog() {
        val tambahMejaLoader = ResHelper.loadFXML("tambahMeja.fxml")
        val scene   = Scene(tambahMejaLoader.load())
        val stage   = Stage()

        stage.title = "Tambah Meja"
        stage.scene = scene
        stage.show()

        val controller = tambahMejaLoader.getController<TambahMejaController>()
        val listener = object : TambahMejaListener {
            override fun onComplete() {
                stage.close()
                setTableData()
                setButtonState(enableUbahStatus = false, enablePilihMeja = false)
            }
        }

        controller.setListener(listener)
    }
}