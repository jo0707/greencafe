package com.jo.greencafe.controller.menu

import com.jo.greencafe.controller.utils.Const
import com.jo.greencafe.controller.utils.listener.MenuListener
import com.jo.greencafe.model.entity.*
import com.jo.greencafe.model.repository.DbRepository
import io.github.palexdev.materialfx.controls.MFXButton
import io.github.palexdev.materialfx.controls.MFXTextField
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.*

class TambahMenuController: Initializable {
    private lateinit var menuListener: MenuListener
    private lateinit var menu: Menu
    private lateinit var listMenu: List<Menu>

    private var mode = ""
    private val repository = DbRepository.getRepository()

    @FXML
    private lateinit var doneBtn: MFXButton

    @FXML
    private lateinit var errorLb: Label

    @FXML
    private lateinit var hargaMenuTf: MFXTextField

    @FXML
    private lateinit var jenisMenuCb: MFXLegacyComboBox<Any>

    @FXML
    private lateinit var namaMenuTf: MFXTextField

    @FXML
    private lateinit var titleLb: Label

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        listMenu = repository.getAllMenu()
        jenisMenuCb.items.addAll("MAKANAN", "MINUMAN")
        jenisMenuCb.selectionModel.selectFirst()
        setButton(false)

        hargaMenuTf.textProperty().addListener { _, _, newValue ->
            setButton(!newValue.contains("\\D".toRegex()) && newValue.isNotBlank())
        }
    }

    @FXML
    fun onDoneBtnClicked(event: ActionEvent) {
        val jenis = jenisMenuCb.value.toString()
        val namaMenu = namaMenuTf.text
        val hargaMenu = hargaMenuTf.text.toInt()

        errorLb.text = when {
            namaMenu.isBlank() -> "Nama Menu kosong, coba lagi"
            listMenu.find { it.namaMenu == namaMenu } != null && mode == Const.CREATE -> "Menu sudah ada!"
            else -> ""
        }

        if (errorLb.text.isNotBlank()) return

        if (mode == Const.CREATE) {
            create(Menu(0, namaMenu, jenis, hargaMenu))
        } else {
            update(Menu(menu.idMenu, namaMenu, jenis, hargaMenu))
        }

        menuListener.onComplete(null)
    }

    private fun setButton(enabled: Boolean) {
        doneBtn.id = if (enabled) Const.PRIMARY else Const.DISABLED
        doneBtn.isDisable = !enabled
    }

    private fun create(menu: Menu) {
        repository.insertMenu(menu)
        repository.insertLog("Penambahaan menu ${menu.namaMenu}")
    }

    private fun update(menu: Menu) {
        repository.updateMenu(menu)
        repository.insertLog("pembaruan menu ${menu.namaMenu}")
    }

    fun set(menuListener: MenuListener, menu: Menu?, mode: String) {
        var title = "Tambah Menu"

        if (mode == Const.UPDATE) {
            title = "Edit Menu"
            this.menu = menu!!

            jenisMenuCb.value = menu.jenisMenu
            namaMenuTf.text = menu.namaMenu
            hargaMenuTf.text = menu.harga.toString()
        }

        titleLb.text = title
        this.mode = mode
        this.menuListener = menuListener
    }
}