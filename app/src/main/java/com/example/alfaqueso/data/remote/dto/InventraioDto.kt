package com.example.alfaqueso.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InventarioDto(
    @Json(name = "id")
    val inventarioId: Int = 0,

    @Json(name = "nombre")
    val nombre: String,

    @Json(name = "stock")
    val stock: Int,

    @Json(name = "precio")
    val precio: Int
)