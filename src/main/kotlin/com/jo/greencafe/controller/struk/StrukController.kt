package com.jo.greencafe.controller.struk

import com.jo.greencafe.model.entity.OrderDisplay
import com.jo.greencafe.model.entity.TransaksiDisplay
import com.jo.greencafe.utils.ResHelper
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.WritableImage
import java.net.URL
import java.util.*


class StrukController: Initializable {
    private lateinit var transaksi: TransaksiDisplay
    private lateinit var listOrder: List<OrderDisplay>

    @FXML
    private lateinit var hargaColumn: TableColumn<Any, Any>

    @FXML
    private lateinit var jumlahColumn: TableColumn<Any, Any>

    @FXML
    private lateinit var kasirLb: Label

    @FXML
    private lateinit var kembaliLb: Label

    @FXML
    private lateinit var menuTable: MFXLegacyTableView<Any>

    @FXML
    private lateinit var namaMenuColumn: TableColumn<Any, Any>

    @FXML
    private lateinit var noTransaksiLb: Label

    @FXML
    private lateinit var noMejaLb: Label

    @FXML
    private lateinit var subtotalColumn: TableColumn<Any, Any>

    @FXML
    private lateinit var tanggalLb: Label

    @FXML
    private lateinit var totalLb: Label

    @FXML
    private lateinit var tunaiLb: Label

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        namaMenuColumn.cellValueFactory = PropertyValueFactory("namaMenu")
        hargaColumn.cellValueFactory = PropertyValueFactory("harga")
        jumlahColumn.cellValueFactory = PropertyValueFactory("jumlah")
        subtotalColumn.cellValueFactory = PropertyValueFactory("subtotal")
    }

    fun setData(transaksi: TransaksiDisplay, listOrder: List<OrderDisplay>) {
        this.transaksi = transaksi
        this.listOrder = listOrder

        menuTable.items.addAll(*listOrder.toTypedArray())

        noTransaksiLb.text = transaksi.noTransaksi
        tanggalLb.text = "Tanggal : " + transaksi.tglTransaksi
        noMejaLb.text = "Nomor Meja : " + transaksi.noMeja

        totalLb.text = "Total : " + transaksi.totalTransaksi
        tunaiLb.text = "Tunai : " + transaksi.totalTunai
        kembaliLb.text = "Kembali : " + (transaksi.totalTunai - transaksi.totalTransaksi)

        kasirLb.text = "KASIR : " + transaksi.namaPegawai

        print()
    }

    private fun print() {
        val snapshot: WritableImage = totalLb.scene.snapshot(null)
        ResHelper.print("STRUK_${noTransaksiLb.text}", snapshot)
    }
}