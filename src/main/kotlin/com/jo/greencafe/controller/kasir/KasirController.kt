package com.jo.greencafe.controller.kasir

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

class KasirController : Initializable {
    companion object {
        const val DASHBOARD = "kasirDashboard"
        const val BUAT_TRANSAKSI = "buatTransaksi"
        const val KELOLA_MEJA = "kelolaMeja"
        const val LAPORAN_TRANSAKSI = "laporanTransaksi"
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
            DASHBOARD to ResHelper.loadFXML("kasirDashboard.fxml"),
            BUAT_TRANSAKSI to ResHelper.loadFXML("kasirBuatTransaksi.fxml"),
            KELOLA_MEJA to ResHelper.loadFXML("kasirKelolaMeja.fxml"),
            LAPORAN_TRANSAKSI to ResHelper.loadFXML("laporanTransaksi.fxml")
        )

        loader.addView(
            MFXLoaderBean.of(DASHBOARD, viewMap[DASHBOARD]!!.location)
                .setBeanToNodeMapper { createToggle("mfx-dashboard", "Dashboard") }.setDefaultRoot(true).get()
        )
        loader.addView(
            MFXLoaderBean.of(BUAT_TRANSAKSI, viewMap[BUAT_TRANSAKSI]!!.location)
                .setBeanToNodeMapper { createToggle("mfx-spreadsheet", "Buat Transaksi") }.get()
        )
        loader.addView(
            MFXLoaderBean.of(KELOLA_MEJA, viewMap[KELOLA_MEJA]!!.location)
                .setBeanToNodeMapper { createToggle("mfx-users", "Kelola Meja") }.get()
        )
        loader.addView(
            MFXLoaderBean.of(LAPORAN_TRANSAKSI, viewMap[LAPORAN_TRANSAKSI]!!.location)
                .setBeanToNodeMapper { createToggle("mfx-calendars", "Laporan Transaksi") }.get()
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