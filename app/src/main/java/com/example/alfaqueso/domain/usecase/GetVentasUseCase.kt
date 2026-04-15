package com.example.alfaqueso.domain.usecase

import com.example.alfaqueso.domain.repository.VentasRepository
import com.example.alfaqueso.domain.model.Venta
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Caso de uso para obtener el historial de ventas.
 * Se encarga de traer la lista y puede ordenarla por fecha.
 */
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