package com.example.alfaqueso.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_ventas")
data class VentaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clienteId: Int,             // <-- Actualizado
    val nombreCliente: String,      // <-- Actualizado
    val total: Double,              // <-- Actualizado (antes montoTotal)
    val metodoPago: String,         // <-- Nuevo campo
    val fecha: Long,                // <-- Actualizado (antes fechaTransaccion)
    val sincronizado: Boolean = false // ¡Lo mantenemos porque es clave para Azure!
)