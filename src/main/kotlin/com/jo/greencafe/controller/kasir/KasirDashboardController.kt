package com.jo.greencafe.controller.kasir

import com.jo.greencafe.model.repository.DbRepository
import com.jo.greencafe.utils.DateHelper
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.*


class KasirDashboardController: Initializable {
    val repository = DbRepository.getRepository()

    @FXML
    private lateinit var namaKasirLb: Label

    @FXML
    private lateinit var penjualanBulanIniLb: Label

    @FXML
    private lateinit var penjualanHariIniLb: Label

    @FXML
    private lateinit var teleponLb: Label

    @FXML
    private lateinit var dateLb: Label



    override fun initialize(location: URL?, resources: ResourceBundle?) {
        namaKasirLb.text = repository.loggedPegawai.namaPegawai
        teleponLb.text = repository.loggedPegawai.telepon

        Timer().schedule(object : TimerTask() {
            override fun run() {
                Platform.runLater {
                    var harian = 0
                    var bulanan = 0

                    repository.getAllTransaksi().forEach {
                        if (it.idPegawai != repository.loggedPegawai.id) return@forEach
                        if (DateHelper.dateTimeIsToday(it.tglTransaksi)) harian += it.totalTransaksi
                        if (DateHelper.dateTimeIsThisMonth(it.tglTransaksi)) bulanan += it.totalTransaksi
                    }

                    penjualanHariIniLb.text = "Rp. ${"%,d".format(harian)}"
                    penjualanBulanIniLb.text = "Rp. ${"%,d".format(bulanan)}"
                }
            }
        }, 0L, 10000L)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                Platform.runLater { dateLb.text = DateHelper.getDisplayDate().replace("-", "\n") }
            }
        }, 0L, 1000L)
    }
}