package com.jo.greencafe.controller.transaksi

import com.jo.greencafe.controller.struk.StrukController
import com.jo.greencafe.controller.utils.Const
import com.jo.greencafe.model.entity.OrderDisplay
import com.jo.greencafe.model.entity.TransaksiDisplay
import com.jo.greencafe.model.repository.DbRepository
import com.jo.greencafe.utils.DateHelper
import com.jo.greencafe.utils.ResHelper
import io.github.palexdev.materialfx.controls.MFXButton
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.DatePicker
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import java.net.URL
import java.util.*

class RiwayatTransaksiController: Initializable {
    val repository = DbRepository.getRepository()
    private val listPegawai = repository.getAllPegawai().filter { it.jabatan == "KASIR" }
    private var listTransaksi = repository.getAllTransaksi()

    private lateinit var selectedTransaksi: TransaksiDisplay

    @FXML
    private lateinit var cetakStrukBtn: MFXButton
    @FXML
    private lateinit var datePicker1: DatePicker
    @FXML
    private lateinit var datePicker2: DatePicker
    @FXML
    private lateinit var hargaColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var jumlahColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var menuTable: MFXLegacyTableView<Any>
    @FXML
    private lateinit var namaMenuColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var namaPegawaiColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var noTransaksiColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var pegawaiCb: MFXLegacyComboBox<Any>
    @FXML
    private lateinit var pendapatanLb: Label
    @FXML
    private lateinit var subtotalColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var tanggalColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var totalColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var transaksiTable: MFXLegacyTableView<Any>
    @FXML
    private lateinit var tunaiColumn: TableColumn<Any, Any>

    fun onExportClicked() {
        val headers = listOf("Nama Pegawai", "No Transaksi", "Total", "Tunai", "Tanggal", "Nomor Meja")
        val fields = listOf("namaPegawai", "noTransaksi", "totalTransaksi", "totalTunai", "tglTransaksi", "noMeja")
        val listTransaksi = transaksiTable.items.toList()

        val filename =  "GreenCafe_Transaksi${if (repository.loggedPegawai.jabatan == "KASIR") "_" + repository.loggedPegawai.namaPegawai else ""}"

        ResHelper.exportToExcel(filename, headers, listTransaksi, fields)
    }

    @FXML
    fun datePicker1OnSelect(event: ActionEvent) {
        setTableData()
    }

    @FXML
    fun datePicker2OnSelect(event: ActionEvent) {
        setTableData()
    }

    @FXML
    fun onCetakStrukClicked(event: ActionEvent) {
        startStrukDialog()
    }

    @FXML
    fun onRefresh(event: ActionEvent) {
        setTableData()
    }

    @FXML
    fun onTableClicked(event: MouseEvent) {
        val transaksi = transaksiTable.selectionModel.selectedItem as TransaksiDisplay?
        if (transaksi != null) {
            selectedTransaksi = transaksi
            setMenuData(transaksi.noTransaksi)
        }
    }

    @FXML
    fun pegawaiCbOnSelect(event: ActionEvent) {
        setTableData()
    }

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        setupPegawaiCb()
        setupTable()
        setTableData()
        setCetakStrukBtn(false)
    }

    private fun setTableData() {
        val date1 = DateHelper.getSimpleDate(datePicker1.value)
        val date2 = DateHelper.getSimpleDate(datePicker2.value)
        val pegawai = listPegawai.find { it.namaPegawai == pegawaiCb.value.toString() }

        transaksiTable.items.clear()
        menuTable.items.clear()

        setCetakStrukBtn(false)

        listTransaksi = repository.getAllTransaksi(date1, date2, pegawai?.id)
        transaksiTable.items.addAll(*listTransaksi.toTypedArray())
        pendapatanLb.text = "Pendapatan : Rp.${listTransaksi.sumOf { it.totalTransaksi }}"
    }

    private fun setMenuData(noTransaksi: String) {
        val listOrder = repository.getOrder(noTransaksi)

        menuTable.items.clear()
        menuTable.items.addAll(*listOrder.toTypedArray())
        setCetakStrukBtn(true)
    }

    private fun setupTable() {
        namaPegawaiColumn.cellValueFactory = PropertyValueFactory("namaPegawai")
        noTransaksiColumn.cellValueFactory = PropertyValueFactory("noTransaksi")
        totalColumn.cellValueFactory = PropertyValueFactory("totalTransaksi")
        tunaiColumn.cellValueFactory = PropertyValueFactory("totalTunai")
        tanggalColumn.cellValueFactory = PropertyValueFactory("tglTransaksi")

        namaMenuColumn.cellValueFactory = PropertyValueFactory("namaMenu")
        hargaColumn.cellValueFactory = PropertyValueFactory("harga")
        jumlahColumn.cellValueFactory = PropertyValueFactory("jumlah")
        subtotalColumn.cellValueFactory = PropertyValueFactory("subtotal")

        datePicker1.converter = DateHelper.stringConverter
        datePicker2.converter = DateHelper.stringConverter

        transaksiTable.items.clear()
        menuTable.items.clear()
    }

    private fun setupPegawaiCb() {
        if (repository.loggedPegawai.jabatan == "KASIR") {
            pegawaiCb.items.add(repository.loggedPegawai.namaPegawai)
        } else {
            pegawaiCb.items.addAll(*(listOf("-") + listPegawai.map { it.namaPegawai }).toTypedArray())
        }

        pegawaiCb.selectionModel.selectFirst()
    }

    private fun setCetakStrukBtn(enabled: Boolean) {
        cetakStrukBtn.isDisable = !enabled
        cetakStrukBtn.id = if (enabled) Const.PRIMARY else Const.DISABLED
    }

    private fun startStrukDialog() {
        val strukLoader = ResHelper.loadFXML("struk.fxml")
        val scene   = Scene(strukLoader.load())
        val stage   = Stage()

        stage.title = Const.getTitle("Struk")
        stage.scene = scene
        stage.show()

        val controller = strukLoader.getController<StrukController>()
        controller.setData(selectedTransaksi, menuTable.items.toList() as List<OrderDisplay>)
    }
}