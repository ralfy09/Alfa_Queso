package com.example.alfaqueso.domain.usecase

data class VentaUseCases(
    val realizarVenta: RealizarVentaUseCase,
    val getVentas: GetVentasUseCase // Asumiendo que lo creaste
)