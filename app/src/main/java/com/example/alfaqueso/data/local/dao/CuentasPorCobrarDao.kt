package com.example.alfaqueso.data.local.dao

import androidx.room.*
import com.example.alfaqueso.data.local.entity.CuentasPorCobrarEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface CuentasPorCobrarDao {
    @Query("SELECT * FROM tabla_cxc ORDER BY fechaCreacion DESC")
    fun obtenerTodas(): Flow<List<CuentasPorCobrarEntity>>

    @Query("SELECT * FROM tabla_cxc WHERE clienteId = :clienteId")
    fun obtenerPorCliente(clienteId: Int): Flow<List<CuentasPorCobrarEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCuenta(cuenta: CuentasPorCobrarEntity)

    @Update
    suspend fun actualizarCuenta(cuenta: CuentasPorCobrarEntity)

    @Query("DELETE FROM tabla_cxc WHERE cuentaId = :id")
    suspend fun eliminarCuenta(id: Int)
}