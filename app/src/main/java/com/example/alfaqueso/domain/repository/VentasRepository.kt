package com.example.alfaqueso.domain.repository

import com.example.alfaqueso.domain.model.Venta
import kotlinx.coroutines.flow.Flow

interface VentasRepository {
    fun obtenerHistorialVentas(): Flow<List<Venta>>
    suspend fun registrarVenta(venta: Venta)
    suspend fun sincronizarVentasConServidor()
}
