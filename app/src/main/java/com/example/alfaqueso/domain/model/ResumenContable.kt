package com.example.alfaqueso.domain.model

data class ResumenContable(
    val totalVentasRealizadas: Int,
    val ingresoTotal: Double,
    val ventasPorProducto: Map<String, Int> // Ej: "Queso de Hoja" -> 15 (vendidos)
)