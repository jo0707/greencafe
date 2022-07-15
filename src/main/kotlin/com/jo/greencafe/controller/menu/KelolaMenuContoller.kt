package com.jo.greencafe.controller.menu

import com.jo.greencafe.controller.utils.Const
import com.jo.greencafe.controller.utils.listener.MenuListener
import com.jo.greencafe.model.entity.Menu
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

class KelolaMenuContoller: Initializable {
    val repository = DbRepository.getRepository()
    
    @FXML
    private lateinit var editBtn: MFXButton

    @FXML
    private lateinit var hapusBtn: MFXButton

    @FXML
    private lateinit var hargaMenuColumn: TableColumn<Any, Any>

    @FXML
    private lateinit var jenisMenuColumn: TableColumn<Any, Any>

    @FXML
    private lateinit var menuTable: MFXLegacyTableView<Any>

    @FXML
    private lateinit var namaMenuColumn: TableColumn<Any, Any>

    @FXML
    fun onEditClicked(event: ActionEvent) {
        val menu = menuTable.selectionModel.selectedItem as Menu?

        if (menu != null)
            startAddDialog(menu, Const.UPDATE)
    }

    @FXML
    fun onHapusClicked(event: ActionEvent) {
        val menu = menuTable.selectionModel.selectedItem as Menu?

        if (menu != null) {
            repository.deleteMenu(menu.idMenu)
            repository.insertLog("Penghapusan menu ${menu.namaMenu}")
            setTableData()
        }
    }

    @FXML
    fun onTableClicked(event: MouseEvent) {
        setButton(menuTable.selectionModel.selectedIndex >= 0)
    }

    @FXML
    fun onTambahClicked(event: ActionEvent) {
        startAddDialog(null, Const.CREATE)
    }

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        setupTable()
        setButton(false)
    }

    private fun startAddDialog(Menu: Menu?, mode: String) {
        val tambahMenuLoader = ResHelper.loadFXML("tambahMenu.fxml")
        val scene   = Scene(tambahMenuLoader.load())
        val stage   = Stage()

        stage.title = Const.getTitle(if (mode == Const.CREATE) "Tambah Menu" else "Edit Menu")
        stage.scene = scene
        stage.show()

        val controller = tambahMenuLoader.getController<TambahMenuController>()
        val listener = object : MenuListener {
            override fun onComplete(menu: Menu?) {
                stage.close()
                setTableData()
            }
        }
        
        controller.set(listener, Menu, mode)
    }

    private fun setTableData() {
        menuTable.items.clear()
        menuTable.items.addAll(*repository.getAllMenu().toTypedArray())
    }

    private fun setButton(enabled: Boolean) {
        editBtn.id = if (enabled) Const.WARNING else Const.DISABLED
        hapusBtn.id = if (enabled) Const.NEGATIVE else Const.DISABLED
        editBtn.isDisable = !enabled
        hapusBtn.isDisable = !enabled
    }

    private fun setupTable() {
        namaMenuColumn.cellValueFactory = PropertyValueFactory("namaMenu")
        hargaMenuColumn.cellValueFactory = PropertyValueFactory("harga")
        jenisMenuColumn.cellValueFactory = PropertyValueFactory("jenisMenu")
        setTableData()
    }
}