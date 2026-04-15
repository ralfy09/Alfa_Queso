package com.example.alfaqueso.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.alfaqueso.data.local.entities.ProductoEntity
import com.example.alfaqueso.domain.model.Inventario
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Upsert
    suspend fun upSertProducto(producto: ProductoEntity)

    @Query("""
    SELECT * FROM tabla_productos
    WHERE id = :id
    LIMIT 1
""")
    suspend fun obtenerProductoById(id: Int): ProductoEntity

    @Query("SELECT * FROM tabla_productos")
    fun obtenerProductos(): Flow<List<ProductoEntity>>

//    @Query("SELECT * FROM tabla_productos")
//    fun obtenerTodoElInventario(): Flow<List<Inventario>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarProductos(productos: List<ProductoEntity>)

    @Query("UPDATE tabla_productos SET inventario = :nuevaCantidad WHERE id = :productoId")
    suspend fun actualizarInventario(productoId: Int, nuevaCantidad: Int)
}