package com.example.alfaqueso.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClienteDto(
    @Json(name = "clienteId") val clienteId: Int = 0,
    @Json(name = "nombreNegocio") val nombreNegocio: String,
    @Json(name = "contacto") val contacto: String,
    @Json(name = "telefono") val telefono: String,
    @Json(name = "email") val email: String,
    @Json(name = "cuentasPorCobrar") val cuentasPorCobrar: List<CuentasPorCobrarDto> = emptyList()
)