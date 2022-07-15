package com.jo.greencafe

import com.jo.greencafe.controller.utils.Const
import com.jo.greencafe.utils.ResHelper
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class GreenCafeApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = ResHelper.loadFXML("login.fxml")
        val scene = Scene(fxmlLoader.load())

        stage.title = Const.getTitle("Login")
        stage.scene = scene
        stage.isResizable = true
        stage.icons.add(Image(this::class.java.getResourceAsStream("img/logo.png")))
        stage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(GreenCafeApplication::class.java)
}