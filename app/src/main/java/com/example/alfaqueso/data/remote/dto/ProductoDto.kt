package com.example.alfaqueso.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductoDto(
    @Json(name = "id") val id: Int,
    @Json(name = "nombre") val nombre: String,
    @Json(name = "precio") val precio: Double,
    @Json(name = "inventario") val inventario: Int,
)