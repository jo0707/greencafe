package com.jo.greencafe.controller.log

import com.jo.greencafe.utils.DateHelper
import com.jo.greencafe.utils.DateHelper.stringConverter
import com.jo.greencafe.model.entity.Log
import com.jo.greencafe.model.entity.Pegawai
import com.jo.greencafe.model.repository.DbRepository
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.DatePicker
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.PropertyValueFactory


class LogAktivitasController {
    private lateinit var listPegawai: List<Pegawai>
    private val repository = DbRepository.getRepository()

    @FXML
    private lateinit var datePicker1: DatePicker

    @FXML
    private lateinit var datePicker2: DatePicker

    @FXML
    private lateinit var logTable: MFXLegacyTableView<Any>

    @FXML
    private lateinit var pegawaiCb: MFXLegacyComboBox<Any>

    @FXML
    private lateinit var namaColumn: TableColumn<Log, String>
    @FXML
    private lateinit var aktivitasColumn: TableColumn<Log, String>
    @FXML
    private lateinit var jabatanColumn: TableColumn<Log, String>
    @FXML
    private lateinit var tanggalColumn: TableColumn<Log, String>

    @FXML
    fun datePicker1OnSelect(event: ActionEvent) {
        setTableData()
    }

    @FXML
    fun datePicker2OnSelect(event: ActionEvent) {
        setTableData()
    }

    @FXML
    fun pegawaiCbOnSelect(event: ActionEvent) {
        val name = pegawaiCb.value.toString()
        setTableData(name = if (name == "-") null else name)
    }

    @FXML
    fun initialize() {
        setupTable()
        setupPegawai()
        setupUI()
    }

    private fun setTableData(name: String? = null) {
        val date1 = DateHelper.getSimpleDate(datePicker1.value)
        val date2 = DateHelper.getSimpleDate(datePicker2.value)

        logTable.items.clear()
        logTable.items.addAll(*repository.getAllLog(date1, date2, name).toTypedArray())
    }

    private fun setupUI() {
        datePicker1.converter = stringConverter
        datePicker2.converter = stringConverter
    }

    private fun setupTable() {
        namaColumn.cellValueFactory = PropertyValueFactory("namaPegawai")
        aktivitasColumn.cellValueFactory = PropertyValueFactory("aktivitas")
        jabatanColumn.cellValueFactory = PropertyValueFactory("jabatan")
        tanggalColumn.cellValueFactory = PropertyValueFactory("tanggal")
        setTableData()
    }

    private fun setupPegawai() {
        listPegawai = repository.getAllPegawai()
        val listNamaPegawai = (listOf("-") + listPegawai.map { it.namaPegawai }).toTypedArray()
        pegawaiCb.items.addAll(*listNamaPegawai)
    }
}
