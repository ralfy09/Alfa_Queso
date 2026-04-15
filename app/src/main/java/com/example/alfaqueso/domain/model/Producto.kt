package com.example.alfaqueso.domain.model

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val inventario: Int,
    val inventarioDisponible: Int,
    val ultimaActualizacion: Long = System.currentTimeMillis()
)