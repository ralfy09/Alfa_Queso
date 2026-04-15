package com.example.alfaqueso.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VentaDto(
    @Json(name = "id")
    val ventaId: Int = 0,

    @Json(name = "clienteId")
    val clienteId: Int,

    @Json(name = "nombreCliente")
    val nombreCliente: String,

    @Json(name = "total")
    val total: Double,

    @Json(name = "metodoPago")
    val metodoPago: String = "Efectivo",

    @Json(name = "fecha")
    val fecha: String,

    @Json(name = "detalles")
    val detalles: List<VentaDetalleDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class VentaDetalleDto(
    @Json(name = "id")
    val detalleId: Int = 0,

    @Json(name = "productoId")
    val productoId: Int,

    @Json(name = "nombreProducto")
    val nombreProducto: String,

    @Json(name = "cantidad")
    val cantidad: Int,

    @Json(name = "precioUnitario")
    val precioUnitario: Double,

    @Json(name = "subtotal")
    val subtotal: Double
)