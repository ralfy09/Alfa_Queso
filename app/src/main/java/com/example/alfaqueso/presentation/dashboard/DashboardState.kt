package com.example.alfaqueso.presentation.dashboard

data class DashboardState(
    val ventasTotalesHistorico: Double = 0.0,
    val ventasTotalesHoy: Double = 0.0,
    val cantidadVentasHoy: Int = 0,            // Cantidad de ventas de hoy
    val totalPedidos: Int = 0,
    val totalClientes: Int = 0,
    val totalStock: Int = 0,                    // Stock de tabla_productos
    val cargando: Boolean = false
)