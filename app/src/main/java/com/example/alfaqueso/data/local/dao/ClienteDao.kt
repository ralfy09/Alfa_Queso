package com.example.alfaqueso.data.local.dao

import androidx.room.*
import com.example.alfaqueso.data.local.entity.ClienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {
    @Query("SELECT * FROM tabla_clientes")
    fun obtenerClientes(): Flow<List<ClienteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCliente(cliente: ClienteEntity)

    @Delete
    suspend fun eliminarCliente(cliente: ClienteEntity)
}