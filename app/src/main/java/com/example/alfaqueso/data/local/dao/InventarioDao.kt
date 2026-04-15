package com.example.alfaqueso.data.local.dao

import androidx.room.*
import com.example.alfaqueso.data.local.entities.InventarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventarioDao {
    @Query("SELECT * FROM inventario ORDER BY nombre ASC")
    fun obtenerTodoElInventario(): Flow<List<InventarioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(producto: InventarioEntity)

    @Delete
    suspend fun eliminarProducto(producto: InventarioEntity)

    @Query("SELECT * FROM inventario WHERE id = :id")
    suspend fun obtenerProductoPorId(id: Int): InventarioEntity?
}