package com.example.alfaqueso.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alfaqueso.data.local.entities.PedidoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {
    // Lee todos los pedidos desde el más reciente al más antiguo
    @Query("SELECT * FROM pedido_table ORDER BY pedidoId DESC")
    fun obtenerTodosLosPedidos(): Flow<List<PedidoEntity>>

    // Guarda un pedido nuevo o lo sobreescribe si ya existe
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPedido(pedido: PedidoEntity)

    // Cambia el estado de un pedido específico (Ej. a "Entregado")
    @Query("UPDATE pedido_table SET estado = :nuevoEstado WHERE pedidoId = :id")
    suspend fun actualizarEstado(id: Int, nuevoEstado: String)
}