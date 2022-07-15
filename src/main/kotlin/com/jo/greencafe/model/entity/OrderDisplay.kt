package com.jo.greencafe.model.entity

data class OrderDisplay(
    var idOrder: Int = 0,
    var idMenu: Int = 0,
    var namaMenu: String = "",
    var harga: Int = 0,
    var noTransaksi: String = "",
    var jumlah: Int = 0,
    var subtotal: Int = 0
) {
    fun toOrder() = Order(idOrder, idMenu, noTransaksi, jumlah, subtotal)
}