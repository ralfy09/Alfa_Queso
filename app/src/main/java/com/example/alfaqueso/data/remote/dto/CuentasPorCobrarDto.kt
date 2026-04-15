package com.example.alfaqueso.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CuentasPorCobrarDto(
    @Json(name = "cuentaId") val cuentaId: Int = 0,
    @Json(name = "clienteId") val clienteId: Int,
    @Json(name = "nombreNegocio") val nombreNegocio: String, // Usaremos este nombre siempre
    @Json(name = "contacto") val contacto: String,
    @Json(name = "estado") val estado: String,
    @Json(name = "montoDeuda") val montoDeuda: Double,
    @Json(name = "totalPorCobrar") val totalPorCobrar: Double,
    @Json(name = "facturasVencidas") val facturasVencidas: Int = 0
)