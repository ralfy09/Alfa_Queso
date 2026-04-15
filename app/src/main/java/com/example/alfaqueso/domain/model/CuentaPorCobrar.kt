package com.example.alfaqueso.domain.model

data class CuentaPorCobrar(
    val id: Int = 0,
    val clienteId: Int,
    val monto: Double,
    val concepto: String,
    val fecha: String,
    val pagada: Boolean = false
)