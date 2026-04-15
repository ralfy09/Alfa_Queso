package com.example.alfaqueso.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "usuario") val usuario: String,
    @Json(name = "clave") val clave: String
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "token") val token: String, // El "pase VIP" que nos da el servidor
    @Json(name = "nombreUsuario") val nombreUsuario: String
)