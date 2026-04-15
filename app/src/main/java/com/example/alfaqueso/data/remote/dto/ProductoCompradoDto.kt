package com.example.alfaqueso.data.remote.dto

import kotlinx.serialization.Serializable
@Serializable
data class ProductoCompraDto(
    val productoId: Int = 0,
    val nombreProducto: String = "",
    val cantidad: Int = 0,
    val precio: Double = 0.0,
    val importe: Double = 0.0
)