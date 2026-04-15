package com.example.alfaqueso.domain.usecase

import com.example.alfaqueso.domain.repository.VentasRepository
import com.example.alfaqueso.domain.repository.CuentasPorCobrarRepository
import com.example.alfaqueso.data.remote.dto.ClienteDto
import com.example.alfaqueso.data.remote.dto.VentaDto
import com.example.alfaqueso.data.mapper.toDomain
import com.example.alfaqueso.domain.repository.InventarioRepository

import javax.inject.Inject

class RealizarVentaUseCase @Inject constructor(
    private val ventasRepository: VentasRepository,
    private val inventarioRepository: InventarioRepository,
    private val cxcRepository: CuentasPorCobrarRepository
) {
    suspend operator fun invoke(ventaDto: VentaDto, clienteDto: ClienteDto) {

        // 1. Convertimos el DTO a modelo de Dominio
        val ventaDominio = ventaDto.toDomain()

        // 2. Registramos la venta
        ventasRepository.registrarVenta(ventaDominio)

        // 3. Actualizamos inventario usando un bucle FOR para evitar líos con suspend
        for (detalle in ventaDto.detalles) {
            inventarioRepository.actualizarInventarioLocal(
                productoId = detalle.productoId,
                nuevaCantidad = -detalle.cantidad
            )
        }

        // 4. Lógica de Cuentas por Cobrar
        if (ventaDto.metodoPago == "Crédito" || ventaDto.metodoPago == "Transferencia") {
            // Aquí ya puedes llamar a cxcRepository sin errores
        }
    }
}