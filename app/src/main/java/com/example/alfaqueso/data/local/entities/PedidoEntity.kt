package com.example.alfaqueso.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedido_table")
data class PedidoEntity(
    @PrimaryKey val pedidoId: Int,
    val clienteId: Int,
    val nombreCliente: String,
    val fechaEntrega: String,
    val notas: String,
    val estado: String
)