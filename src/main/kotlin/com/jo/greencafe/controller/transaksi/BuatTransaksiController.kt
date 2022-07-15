package com.jo.greencafe.controller.transaksi

import com.jo.greencafe.controller.order.TambahOrderController
import com.jo.greencafe.controller.meja.KelolaMejaController
import com.jo.greencafe.controller.struk.StrukController
import com.jo.greencafe.controller.utils.Const
import com.jo.greencafe.controller.utils.listener.OrderDisplayListener
import com.jo.greencafe.controller.utils.listener.MejaListener
import com.jo.greencafe.model.entity.OrderDisplay
import com.jo.greencafe.model.entity.Meja
import com.jo.greencafe.model.entity.TransaksiDisplay
import com.jo.greencafe.model.repository.DbRepository
import com.jo.greencafe.utils.DateHelper
import com.jo.greencafe.utils.DialogHelper
import com.jo.greencafe.utils.ResHelper
import io.github.palexdev.materialfx.controls.MFXButton
import io.github.palexdev.materialfx.controls.MFXTextField
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView
import javafx.application.Platform
import javafx.collections.ListChangeListener
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


class BuatTransaksiController: Initializable {
    val repository = DbRepository.getRepository()
    private lateinit var selectedMeja: Meja
    private lateinit var noTransaksi: String

    @FXML
    private lateinit var namaMenuColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var bayarBtn: MFXButton
    @FXML
    private lateinit var bayarTf: MFXTextField
    @FXML
    private lateinit var hapusMenuBtn: MFXButton
    @FXML
    private lateinit var hargaColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var kembaliTf: MFXTextField
    @FXML
    private lateinit var menuTable: MFXLegacyTableView<Any>
    @FXML
    private lateinit var subtotalColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var namaKasirLb: Label
    @FXML
    private lateinit var noTransaksiLb: Label
    @FXML
    private lateinit var pilihMejaBtn: MFXButton

    @FXML
    private lateinit var tanggalLb: Label
    @FXML
    private lateinit var jumlahColumn: TableColumn<Any, Any>
    @FXML
    private lateinit var totalTf: MFXTextField

    @FXML
    fun onBayarBtnClicked(event: ActionEvent) {
        try {
            val totalTransaksi  = menuTable.items.toList().sumOf { (it as OrderDisplay).subtotal }
            val totalTunai      = bayarTf.text.toInt()
            val listOrder      = menuTable.items.toList() as List<OrderDisplay>
            val transaksi       = TransaksiDisplay(0, repository.loggedPegawai.id, repository.loggedPegawai.namaPegawai, noTransaksi, totalTransaksi, totalTunai, selectedMeja.idMeja, selectedMeja.noMeja, DateHelper.getDateTime())

            repository.insertTransaksi(transaksi.toTransaksi(), listOrder.map { it.toOrder() })
            repository.insertLog("Pembuatan transaksi $noTransaksi oleh ${repository.loggedPegawai.namaPegawai}")

            resetInput()
            startStrukDialog(transaksi, listOrder)
        } catch (e: NumberFormatException) {
            DialogHelper.showErrorMessage("Input hanya menerima angka.")
        } catch (e: Exception) {
            DialogHelper.showErrorMessage("Pembuatan Transaksi gagal\n\n${e.message}")
        }
    }

    @FXML
    fun onHapusMenuBtn(event: ActionEvent) {
        val order = menuTable.selectionModel.selectedItem as OrderDisplay?

        if (order != null)
            menuTable.items.remove(order)
    }

    @FXML
    fun onTableClicked(event: MouseEvent) {
        setHapusMenuBtn(true)
    }

    @FXML
    fun onTambahMenuClicked(event: ActionEvent) {
        startOrderDialog()
    }

    @FXML
    fun onPilihMejaBtnClicked(event: ActionEvent) {
        startMejaDialog()
    }

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                Platform.runLater { tanggalLb.text = DateHelper.getDisplayDate() }
            }
        }, 0L, 1000L)

        resetInput()
        setupTable()

        bayarTf.textProperty().addListener { _, _, newValue ->
            if (newValue.isEmpty() || newValue.contains("\\D".toRegex()))
                setBayarBtn(false)
            else {
                isTransactionValid()
            }
        }
    }

    private fun resetInput() {
        namaKasirLb.text = repository.loggedPegawai.namaPegawai
        val lastNoTransaksi = repository.getLastNoTransaksi()

        noTransaksi = "BISA-" + if (!lastNoTransaksi.isNullOrEmpty())
            lastNoTransaksi.replace("\\D".toRegex(), "").toInt() + 1 else 1

        noTransaksiLb.text = noTransaksi
        bayarTf.text = ""
        totalTf.text = ""
        kembaliTf.text = ""
        pilihMejaBtn.text = "Pilih Meja"

        menuTable.items.clear()

        setHapusMenuBtn(false)
        setBayarBtn(false)
        setBayarTfEnabled(false)
    }

    private fun isTransactionValid() {
        val mejaValid = pilihMejaBtn.text.contains("\\d".toRegex())
        val menuValid = menuTable.items.isNotEmpty()
        val bayarValid = bayarTf.text.isNotEmpty() && !bayarTf.text.contains("\\D".toRegex())

        val bayar = bayarTf.text.toIntOrNull()
        val total = totalTf.text.toIntOrNull()
        val kembali = if (bayar != null && total != null) bayar - total else null
        val kembaliValid = (kembali != null) && kembali >= 0

        setKembalian(kembali)
        setBayarBtn(mejaValid && menuValid && bayarValid && kembaliValid)
    }

    private fun setKembalian(kembali: Int?) {
        kembaliTf.text = kembali?.toString() ?: ""
    }

    private fun setHapusMenuBtn(enabled: Boolean) {
        hapusMenuBtn.isDisable = !enabled
        hapusMenuBtn.id = if (enabled) Const.NEGATIVE else Const.DISABLED
    }

    private fun setBayarBtn(enabled: Boolean) {
        bayarBtn.isDisable = !enabled
        bayarBtn.id = if (enabled) Const.PRIMARY else Const.DISABLED
    }

    private fun setBayarTfEnabled(enabled: Boolean) {
        bayarTf.text = if (enabled) bayarTf.text else ""
        bayarTf.isDisable = !enabled
    }

    private fun setupTable() {
        namaMenuColumn.cellValueFactory = PropertyValueFactory("namaMenu")
        hargaColumn.cellValueFactory = PropertyValueFactory("harga")
        jumlahColumn.cellValueFactory = PropertyValueFactory("jumlah")
        subtotalColumn.cellValueFactory = PropertyValueFactory("subtotal")

        menuTable.items.addListener(ListChangeListener { c ->
            val list = c?.list as List<OrderDisplay>?

            if (!list.isNullOrEmpty()) {
                var total = 0
                list.forEach{ total += it.subtotal }
                totalTf.text = total.toString()

                setBayarTfEnabled(true)
            } else {
                setBayarTfEnabled(false)
            }

            setHapusMenuBtn(false)
            isTransactionValid()
        })
    }

    private fun startMejaDialog() {
        val mejaLoader = ResHelper.loadFXML("kasirKelolaMeja.fxml")
        val scene   = Scene(mejaLoader.load())
        val stage   = Stage()

        stage.title = "Pilih Meja"
        stage.scene = scene
        stage.show()

        val controller = mejaLoader.getController<KelolaMejaController>()
        val listener = object : MejaListener {
            override fun onComplete(meja: Meja) {
                stage.close()
                selectedMeja = meja
                pilihMejaBtn.text = meja.noMeja
                isTransactionValid()
            }
        }

        controller.pick(listener)
    }

    private fun startOrderDialog() {
        val mejaLoader = ResHelper.loadFXML("kasirTambahOrder.fxml")
        val scene   = Scene(mejaLoader.load())
        val stage   = Stage()

        stage.title = "Pilih Menu"
        stage.scene = scene
        stage.show()

        val controller = mejaLoader.getController<TambahOrderController>()
        val listener = object : OrderDisplayListener {
            override fun onComplete(order: OrderDisplay) {
                stage.close()

                val menuFromTable = menuTable.items.find { (it as OrderDisplay).idMenu == order.idMenu } as OrderDisplay?
                if (menuFromTable != null) {
                    menuFromTable.jumlah += order.jumlah
                    menuFromTable.subtotal += order.subtotal
                } else {
                    menuTable.items.add(order)
                }

                menuTable.refresh()
            }
        }

        controller.initListener(listener, noTransaksi)
    }

    private fun startStrukDialog(transaksi: TransaksiDisplay, listOrder: List<OrderDisplay>) {
        val strukLoader = ResHelper.loadFXML("struk.fxml")
        val scene   = Scene(strukLoader.load())
        val stage   = Stage()

        stage.title = Const.getTitle("Struk")
        stage.scene = scene
        stage.show()

        val controller = strukLoader.getController<StrukController>()
        controller.setData(transaksi, listOrder)
    }
}