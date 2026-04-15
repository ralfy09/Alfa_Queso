package com.example.alfaqueso.domain.repository

import com.example.alfaqueso.data.remote.dto.CuentasPorCobrarDto
import kotlinx.coroutines.flow.Flow

interface CuentasPorCobrarRepository {
    fun obtenerTodas(): Flow<List<CuentasPorCobrarDto>>
    suspend fun crearCuenta(cuenta: CuentasPorCobrarDto)
    suspend fun actualizarEstado(cuenta: CuentasPorCobrarDto)
}