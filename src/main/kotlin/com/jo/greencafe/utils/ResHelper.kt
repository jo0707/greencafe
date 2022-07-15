package com.jo.greencafe.utils

import com.jo.greencafe.GreenCafeApplication
import io.github.palexdev.materialfx.utils.SwingFXUtils
import javafx.fxml.FXMLLoader
import javafx.scene.control.Control
import javafx.scene.image.WritableImage
import javafx.stage.Stage
import org.jxls.template.SimpleExporter
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import javax.imageio.ImageIO
import javax.swing.filechooser.FileSystemView


object ResHelper {
    fun loadFXML(filename: String) = FXMLLoader(GreenCafeApplication::class.java.getResource(filename))
    fun getStage(c: Control) = c.scene.window as Stage


    fun exportToExcel(filename: String, headers: List<String>, listData: List<Any>, fields: List<String>) {
        try {
            val dir = getDocumentPathAsXls(filename)
            val os = getExcelOutputStream(dir)
            SimpleExporter().gridExport(headers, listData, fields.joinToString(","), os)
            Runtime.getRuntime().exec("explorer.exe /select,$dir")
        } catch (e: Exception) {
            DialogHelper.showErrorMessage("Gagal mengekspor data pegawai ke Excel : \n\n${e.message}")
        }
    }

    private fun getDocumentPathAsXls(mFilename: String): String {
        val filename = mFilename + "_" + DateHelper.getFileDate() + ".xls"
        return FileSystemView.getFileSystemView().defaultDirectory.path + "\\$filename"
    }

    private fun getDocumentPathAsPng(mFilename: String): String {
        val filename = mFilename + "_" + ".png"
        return FileSystemView.getFileSystemView().defaultDirectory.path + "\\$filename"
    }

    fun print(filename: String, image: WritableImage) {
        val path = getDocumentPathAsPng(filename)
        val file = File(path)
        val bufferedImage = SwingFXUtils.fromFXImage(image, null)
        ImageIO.write(bufferedImage, "png", file)

        Desktop.getDesktop().print(file)
    }

    @Throws
    private fun getExcelOutputStream(path: String): FileOutputStream = FileOutputStream(path)

}