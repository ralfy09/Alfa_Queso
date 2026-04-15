package com.example.alfaqueso.domain.model

data class ClienteDto(
    val clienteId: Int = 0,
    val nombreNegocio: String,
    val contacto: String,
    val telefono: String,
    val email: String,
    val cuentasPorCobrar: List<CuentaPorCobrar> = emptyList()
)