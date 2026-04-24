package com.example.alfaqueso.presentation.dashboard

data class DashboardState(
    val ventasTotalesHoy: Double = 0.0,
    val cantidadVentasHoy: Int = 0,
    val totalPedidos: Int = 0,
    val totalClientes: Int = 0,
    val totalStock: Int = 0,
    val cargando: Boolean = false
)