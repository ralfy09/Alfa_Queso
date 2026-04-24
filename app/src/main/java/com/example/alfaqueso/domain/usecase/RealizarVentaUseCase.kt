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

        val ventaDominio = ventaDto.toDomain()

        ventasRepository.registrarVenta(ventaDominio)

        for (detalle in ventaDto.detalles) {
            inventarioRepository.actualizarInventarioLocal(
                productoId = detalle.productoId,
                nuevaCantidad = -detalle.cantidad
            )
        }

        if (ventaDto.metodoPago == "Crédito" || ventaDto.metodoPago == "Transferencia") {
            // Aquí ya puedes llamar a cxcRepository sin errores
        }
    }
}