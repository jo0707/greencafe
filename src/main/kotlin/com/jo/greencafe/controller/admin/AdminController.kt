package com.jo.greencafe.controller.admin

import com.jo.greencafe.utils.ResHelper
import io.github.palexdev.materialfx.controls.MFXIconWrapper
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode
import io.github.palexdev.materialfx.controls.MFXScrollPane
import io.github.palexdev.materialfx.utils.ScrollUtils
import io.github.palexdev.materialfx.utils.ToggleButtonsUtil
import io.github.palexdev.materialfx.utils.others.loader.MFXLoader
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*
import java.util.function.Consumer

class AdminController : Initializable {
    companion object {
        const val DASHBOARD = "AdminDashboard"
        const val KELOLA_AKUN = "KelolaAkun"
        const val KELOLA_PEGAWAI = "KelolaPegawai"
        const val LOG_AKTIVITAS = "LogAktivitas"
    }

    private lateinit var toggleGroup: ToggleGroup

    @FXML
    private lateinit var contentPane: StackPane

    @FXML
    private lateinit var navBar: VBox

    @FXML
    private lateinit var scrollPane: MFXScrollPane

    @FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        this.toggleGroup = ToggleGroup()
        ToggleButtonsUtil.addAlwaysOneSelectedSupport(toggleGroup)
        ScrollUtils.addSmoothScrolling(scrollPane)

        initializeLoader()
    }

    @FXML
    fun onLogoutBtnClicked(event: ActionEvent) {
        ResHelper.getStage(scrollPane).scene = Scene(ResHelper.loadFXML("login.fxml").load())
    }

    private fun initializeLoader() {
        val loader = MFXLoader()

        val viewMap = mapOf(
            DASHBOARD to ResHelper.loadFXML("adminDashboard.fxml"),
            KELOLA_AKUN to ResHelper.loadFXML("kelolaAkun.fxml"),
            KELOLA_PEGAWAI to ResHelper.loadFXML("kelolaPegawai.fxml"),
            LOG_AKTIVITAS to ResHelper.loadFXML("logAktivitas.fxml")
        )

        loader.addView(
            MFXLoaderBean.of(DASHBOARD, viewMap[DASHBOARD]!!.location)
                .setBeanToNodeMapper { createToggle("mfx-dashboard", "Dashboard") }.setDefaultRoot(true).get()
        )
        loader.addView(
            MFXLoaderBean.of(KELOLA_AKUN, viewMap[KELOLA_AKUN]!!.location)
                .setBeanToNodeMapper { createToggle("mfx-lock-open", "Kelola Akun") }.get()
        )
        loader.addView(
            MFXLoaderBean.of(KELOLA_PEGAWAI, viewMap[KELOLA_PEGAWAI]!!.location)
                .setBeanToNodeMapper { createToggle("mfx-users", "Kelola Pegawai") }.get()
        )
        loader.addView(
            MFXLoaderBean.of(LOG_AKTIVITAS, viewMap[LOG_AKTIVITAS]!!.location)
                .setBeanToNodeMapper { createToggle("mfx-eye", "Log Aktivitas") }.get()
        )

        loader.onLoadedAction = Consumer { beans ->
            val navItems: List<ToggleButton> = beans.map { bean ->
                val navItem = bean.beanToNodeMapper.get() as ToggleButton

                navItem.onAction = EventHandler {
                    contentPane.children.setAll(bean.root)
                }

                if (bean.isDefaultView) {
                    contentPane.children.setAll(bean.root)
                    navItem.isSelected = true
                }

                navItem
            }.toList()

            navBar.children.setAll(navItems)
        }

        loader.start()
    }

    private fun createToggle(icon: String, text: String): ToggleButton {
        val wrapper = MFXIconWrapper(icon, 24.0, 32.0)
        val toggleNode = MFXRectangleToggleNode(text, wrapper)
        toggleNode.alignment = Pos.CENTER_LEFT
        toggleNode.maxWidth = Double.MAX_VALUE
        toggleNode.toggleGroup = toggleGroup
        return toggleNode
    }
}