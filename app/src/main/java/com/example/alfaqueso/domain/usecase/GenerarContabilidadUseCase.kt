package com.example.alfaqueso.domain.usecase

import com.example.alfaqueso.domain.repository.VentasRepository
import com.example.alfaqueso.domain.model.ResumenContable
import com.example.alfaqueso.domain.model.Venta
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GenerarContabilidadUseCase @Inject constructor(
    private val ventasRepository: VentasRepository
) {
    operator fun invoke(): Flow<ResumenContable> {
        return ventasRepository.obtenerHistorialVentas().map { listaVentas: List<Venta> ->

            val totalVentas = listaVentas.size

            // 1. Especificamos que queremos sumar Doubles (Dinero)
            // Cambiamos 'montoTotal' por 'total' para que coincida con tu modelo Venta
            val ingreso = listaVentas.sumOf { venta -> venta.total }

            // 2. Agrupamos por el nombre del cliente (o negocio)
            // Usamos '1' para contar ventas, o podrías añadir 'cantidad' a tu modelo Venta
            val ventasPorTipo = listaVentas
                .groupBy { it.nombreCliente }
                .mapValues { entry ->
                    // Sumamos cuántas ventas se hicieron a ese cliente
                    entry.value.size
                }

            ResumenContable(
                totalVentasRealizadas = totalVentas,
                ingresoTotal = ingreso,
                ventasPorProducto = ventasPorTipo
            )
        }
    }
}