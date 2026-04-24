package com.example.alfaqueso.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alfaqueso.data.local.entity.VentaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaDao {
    @Query("SELECT * FROM tabla_ventas ORDER BY fecha DESC")
    fun obtenerHistorialVentas(): Flow<List<VentaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registrarVenta(venta: VentaEntity)

    // Busca las ventas que hicimos cuando no había internet
    @Query("SELECT * FROM tabla_ventas WHERE sincronizado = 0")
    suspend fun obtenerVentasNoSincronizadas(): List<VentaEntity>

    // Marca las ventas como subidas para no mandarlas dos veces a Azure
    @Query("UPDATE tabla_ventas SET sincronizado = 1 WHERE id IN (:ids)")
    suspend fun marcarComoSincronizadas(ids: List<Int>)
}