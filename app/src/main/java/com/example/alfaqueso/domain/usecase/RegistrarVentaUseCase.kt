package com.example.alfaqueso.domain.usecase

// ESTOS SON LOS IMPORT QUE FALTAN O ESTÁN MAL
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

        // Creamos el objeto Venta asegurándonos de que los nombres coincidan con tu modelo
        val nuevaVenta = Venta(
            id = 0,
            clienteId = 0, // O el ID que corresponda
            nombreCliente = "Venta General",
            total = total,
            metodoPago = "Efectivo",
            fecha = System.currentTimeMillis()
        )

        // 1. Aquí se quita el error de 'registrarVenta'
        ventasRepository.registrarVenta(nuevaVenta)

        // 2. Aquí se quita el error de 'actualizarInventarioLocal'
        // Fíjate si en tu interfaz de InventarioRepository el método se llama exactamente así
        inventarioRepository.actualizarInventarioLocal(producto.id, -cantidadVender)
    }
}