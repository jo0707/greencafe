package com.jo.greencafe.controller.order

import com.jo.greencafe.controller.utils.Const
import com.jo.greencafe.controller.utils.listener.OrderDisplayListener
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

class TambahOrderController: Initializable {
    private val repository = DbRepository.getRepository()
    private lateinit var listener: OrderDisplayListener
    private lateinit var listMenu: List<Menu>
    private lateinit var noTransaksi: String
    private lateinit var selectedMenu: Menu

    @FXML
    private lateinit var hargaLb: Label

    @FXML
    private lateinit var jumlahTf: MFXTextField

    @FXML
    private lateinit var namaMenuCb: MFXLegacyComboBox<Any>

    @FXML
    private lateinit var subtotalLb: Label

    @FXML
    private lateinit var tambahBtn: MFXButton

    @FXML
    fun onNamaMenuSelected(event: ActionEvent) {
        val selectedMenuz = listMenu.find { it.namaMenu == namaMenuCb.value.toString() }

        if (selectedMenuz != null) {
            selectedMenu = selectedMenuz
            hargaLb.text = selectedMenu.harga.toString()
            calculateSubTotal()
        }
    }

    @FXML
    fun onTambahClicked(event: ActionEvent) {
        val order = OrderDisplay(0, selectedMenu.idMenu, selectedMenu.namaMenu, selectedMenu.harga, noTransaksi, jumlahTf.text.toInt(), subtotalLb.text.toInt())
        listener.onComplete(order)
    }

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        listMenu = repository.getAllMenu()
        namaMenuCb.items.addAll(*listMenu.map { it.namaMenu }.toTypedArray())

        setButton(false)

        jumlahTf.textProperty().addListener { _, _, newValue ->
            val isValid = !newValue.contains("\\D".toRegex())

            if (isValid) calculateSubTotal()

            setButton(isValid && newValue.isNotBlank())
        }
    }

    private fun calculateSubTotal() {
        try {
            subtotalLb.text = (hargaLb.text.toInt() * jumlahTf.text.toInt()).toString()
        } catch (_: Exception) { }

    }

    private fun setButton(enabled: Boolean) {
        tambahBtn.id = if (enabled) Const.PRIMARY else Const.DISABLED
        tambahBtn.isDisable = !enabled
    }

    fun initListener(listener: OrderDisplayListener, noTransaksi: String) {
        this.listener = listener
        this.noTransaksi = noTransaksi
    }
}