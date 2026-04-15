package com.example.alfaqueso.presentation.dashboard

import com.example.alfaqueso.domain.model.Producto

data class DashboardState(
    val ventasTotalesHoy: Double = 0.0,
    val cantidadVentasHoy: Int = 0,
    val productosBajoStock: List<Producto> = emptyList(),
    val cargando: Boolean = false
)