package com.example.alfaqueso.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_ventas")
data class VentaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clienteId: Int,
    val nombreCliente: String,
    val total: Double,
    val metodoPago: String,
    val fecha: Long,
    val sincronizado: Boolean = false
)