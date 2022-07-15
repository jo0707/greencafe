package com.jo.greencafe.controller.meja

import com.jo.greencafe.controller.utils.Const
import com.jo.greencafe.controller.utils.listener.TambahMejaListener
import com.jo.greencafe.model.repository.DbRepository
import io.github.palexdev.materialfx.controls.MFXButton
import io.github.palexdev.materialfx.controls.MFXTextField
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.*

class TambahMejaController: Initializable {
    val repository = DbRepository.getRepository()
    private lateinit var tambahMejaListener: TambahMejaListener

    @FXML
    private lateinit var doneBtn: MFXButton

    @FXML
    private lateinit var noMejaTf: MFXTextField

    @FXML
    fun onDoneBtnClicked(event: ActionEvent) {
        val noMeja = "M" + noMejaTf.text

        repository.addMeja(noMeja)
        tambahMejaListener.onComplete()
    }

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        setButton(false)

        noMejaTf.textProperty().addListener { _, _, newValue ->
            val isValid = !newValue.contains("\\D".toRegex())
            setButton(isValid && newValue.isNotBlank())

            if (isValid) {
                if (repository.getAllMeja().any { it.noMeja == "M$newValue" }) {
                    setButton(false)
                }
            }
        }
    }

    private fun setButton(enabled: Boolean) {
        doneBtn.id = if (enabled) Const.PRIMARY else Const.DISABLED
        doneBtn.isDisable = !enabled
    }

    fun setListener(tambahMejaListener: TambahMejaListener) {
        this.tambahMejaListener = tambahMejaListener
    }
}