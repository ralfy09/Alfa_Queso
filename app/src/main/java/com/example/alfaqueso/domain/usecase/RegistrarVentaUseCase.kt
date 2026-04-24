package com.example.alfaqueso.domain.usecase


import com.example.alfaqueso.domain.repository.VentasRepository
import com.example.alfaqueso.domain.repository.InventarioRepository
import com.example.alfaqueso.domain.model.Producto
import com.example.alfaqueso.domain.model.Venta
import javax.inject.Inject

class RegistrarVentaUseCase @Inject constructor(
    private val ventasRepository: VentasRepository,
    private val inventarioRepository: InventarioRepository
) {
    suspend operator fun invoke(producto: Producto, cantidadVender: Int) {

        if (cantidadVender > producto.inventarioDisponible) {
            throw Exception("No hay suficiente inventario de ${producto.nombre}")
        }

        val total = producto.precio * cantidadVender

        val nuevaVenta = Venta(
            id = 0,
            clienteId = 0, // O el ID que corresponda
            nombreCliente = "Venta General",
            total = total,
            metodoPago = "Efectivo",
            fecha = System.currentTimeMillis()
        )

        ventasRepository.registrarVenta(nuevaVenta)


        inventarioRepository.actualizarInventarioLocal(producto.id, -cantidadVender)
    }
}