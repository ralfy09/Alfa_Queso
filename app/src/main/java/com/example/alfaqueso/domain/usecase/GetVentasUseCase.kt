package com.example.alfaqueso.domain.usecase

import com.example.alfaqueso.domain.repository.VentasRepository
import com.example.alfaqueso.domain.model.Venta
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetVentasUseCase @Inject constructor(
    private val repository: VentasRepository
) {
    operator fun invoke(): Flow<List<Venta>> {
        return repository.obtenerHistorialVentas().map { lista ->
            // Ordenamos para que las ventas más nuevas aparezcan arriba
            lista.sortedByDescending { it.fecha }
        }
    }
}