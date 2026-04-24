package com.example.alfaqueso.data.repository

import com.example.alfaqueso.data.local.dao.CuentasPorCobrarDao
import com.example.alfaqueso.data.local.entity.CuentasPorCobrarEntity // IMPORTANTE: Importa la Entidad
import com.example.alfaqueso.data.remote.dto.CuentasPorCobrarDto
import com.example.alfaqueso.domain.repository.CuentasPorCobrarRepository
import com.example.alfaqueso.data.mapper.toDto
import com.example.alfaqueso.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CuentasPorCobrarRepositoryImpl @Inject constructor(
    private val dao: CuentasPorCobrarDao
) : CuentasPorCobrarRepository {

    override fun obtenerTodas(): Flow<List<CuentasPorCobrarDto>> {
        return dao.obtenerTodas().map { listaEntidades: List<CuentasPorCobrarEntity> ->
            listaEntidades.map { entidad: CuentasPorCobrarEntity ->
                entidad.toDto()
            }
        }
    }

    override suspend fun crearCuenta(cuenta: CuentasPorCobrarDto) {
        dao.insertarCuenta(cuenta.toEntity())
    }

    override suspend fun actualizarEstado(cuenta: CuentasPorCobrarDto) {
        dao.actualizarCuenta(cuenta.toEntity())
    }
}