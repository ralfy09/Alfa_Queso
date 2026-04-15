package com.example.alfaqueso.domain.model

data class Venta(
    val id: Int = 0,
    val clienteId: Int,
    val nombreCliente: String,
    val total: Double,
    val metodoPago: String,
    val fecha: Long
)