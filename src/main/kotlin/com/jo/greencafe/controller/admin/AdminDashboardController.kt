package com.jo.greencafe.controller.admin

import com.jo.greencafe.model.repository.DbRepository
import com.jo.greencafe.utils.DateHelper
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.*


class AdminDashboardController: Initializable {
    val repository = DbRepository.getRepository()

    @FXML
    private lateinit var dateLb: Label

    @FXML
    private lateinit var meetlb: Label


    override fun initialize(location: URL?, resources: ResourceBundle?) {
        meetlb.text = "Halo, ${repository.loggedPegawai.namaPegawai}, anda login sebagai Admin"

        Timer().schedule(object : TimerTask() {
            override fun run() {
                Platform.runLater { dateLb.text = DateHelper.getDisplayDate()  }
            }
        }, 0L, 1000L)
    }
}