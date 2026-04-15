package com.example.alfaqueso.domain.model

data class DetallePedido(
    val productoId: Int,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Double
) {
    val subtotal: Double
        get() = cantidad * precioUnitario
}