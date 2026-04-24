package com.example.alfaqueso.data.repository

import com.example.alfaqueso.domain.repository.VentasRepository
import com.example.alfaqueso.data.local.dao.VentaDao
import com.example.alfaqueso.data.local.entity.VentaEntity
import com.example.alfaqueso.data.remote.QuesoApi
import com.example.alfaqueso.domain.model.Venta
import com.example.alfaqueso.data.mapper.toDomain
import com.example.alfaqueso.data.mapper.toDto
import com.example.alfaqueso.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VentasRepositoryImpl @Inject constructor(
    private val dao: VentaDao,
    private val api: QuesoApi
) : VentasRepository {

    override fun obtenerHistorialVentas(): Flow<List<Venta>> {
        return dao.obtenerHistorialVentas().map { entidades: List<VentaEntity> ->
            entidades.map { entidad: VentaEntity -> entidad.toDomain() }
        }
    }

    override suspend fun registrarVenta(venta: Venta) {
        dao.registrarVenta(venta.toEntity())
    }

    override suspend fun sincronizarVentasConServidor() {
        try {
            val ventasPendientes = dao.obtenerVentasNoSincronizadas()

            if (ventasPendientes.isNotEmpty()) {
                val idsSincronizados = mutableListOf<Int>()

                for (ventaEntity in ventasPendientes) {
                    val ventaDominio: Venta = ventaEntity.toDomain()
                    api.registrarVentaEnNube(ventaDominio.toDto())

                    idsSincronizados.add(ventaEntity.id)
                }

                dao.marcarComoSincronizadas(idsSincronizados)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}