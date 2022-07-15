package com.jo.greencafe.controller.pegawai

import com.jo.greencafe.controller.utils.listener.PegawaiListener
import com.jo.greencafe.controller.utils.Const
import com.jo.greencafe.model.entity.Pegawai
import com.jo.greencafe.model.repository.DbRepository
import com.jo.greencafe.utils.InputValidator
import io.github.palexdev.materialfx.controls.MFXButton
import io.github.palexdev.materialfx.controls.MFXTextField
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.*

class TambahPegawaiController: Initializable {
    private lateinit var pegawaiListener: PegawaiListener
    private lateinit var pegawai: Pegawai
    private var mode = ""
    private val repository = DbRepository.getRepository()

    @FXML
    private lateinit var alamatTf: MFXTextField

    @FXML
    private lateinit var jabatanCb: MFXLegacyComboBox<Any>

    @FXML
    private lateinit var jenkelCb: MFXLegacyComboBox<Any>

    @FXML
    private lateinit var namaTf: MFXTextField

    @FXML
    private lateinit var teleponTf: MFXTextField

    @FXML
    private lateinit var errorLb: Label

    @FXML
    private lateinit var titleLb: Label

    @FXML
    fun onDoneBtnClicked(event: ActionEvent) {
        val name = namaTf.text
        val telepon = teleponTf.text
        val alamat = alamatTf.text
        val jenkel = jenkelCb.selectionModel.selectedItem as String?
        val jabatan = jabatanCb.selectionModel.selectedItem as String?

        errorLb.text = when {
            name.isBlank() -> "Nama kosong, coba lagi"
            telepon.isBlank() -> "Telepon kosong, coba lagi"
            !InputValidator.isPhoneValid(telepon) -> "Telepon tidak valid. Coba lagi"
            alamat.isBlank() -> "Alamat kosong, coba lagi"
            jenkel.isNullOrBlank() -> "Jenis Kelamin kosong, coba lagi"
            jabatan.isNullOrBlank() -> "Jabatan kosong, coba lagi"
            else -> ""
        }

        if (errorLb.text.isNotBlank()) return

        if (mode == Const.CREATE) {
            create(Pegawai(0, name, jenkel!!, alamat, telepon, jabatan!!))
        } else {
            update(Pegawai(pegawai.id, name, jenkel!!, alamat, telepon, jabatan!!))
        }

        pegawaiListener.onComplete()
    }

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        jenkelCb.items.addAll("Laki-Laki", "Perempuan")
        jabatanCb.items.setAll("KASIR", "MANAGER", "ADMIN")
    }

    private fun create(pegawai: Pegawai) {
        repository.insertPegawai(pegawai)
        repository.insertLog("Penambahaan pegawai ${pegawai.namaPegawai}")
    }

    private fun update(pegawai: Pegawai) {
        repository.updatePegawai(pegawai)
        repository.insertLog("pembaruan pegawai ${pegawai.namaPegawai} (id : ${pegawai.id})")
    }

    fun set(pegawaiListener: PegawaiListener, pegawai: Pegawai?, mode: String) {
        var title = "Tambah Pegawai"

        if (mode == Const.UPDATE) {
            title = "Edit Pegawai"
            this.pegawai = pegawai!!

            namaTf.text = pegawai.namaPegawai
            teleponTf.text = pegawai.telepon
            alamatTf.text = pegawai.alamat
            jenkelCb.value = pegawai.jenkel
            jabatanCb.value = pegawai.jabatan
        }

        titleLb.text = title
        this.mode = mode
        this.pegawaiListener = pegawaiListener
    }
}