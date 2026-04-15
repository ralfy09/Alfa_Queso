package com.example.alfaqueso.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_clientes")
data class ClienteEntity(
    @PrimaryKey(autoGenerate = true)
    val clienteId: Int = 0,
    val nombreNegocio: String,
    val contacto: String,
    val telefono: String,
    val email: String,
    val fechaRegistro: Long = System.currentTimeMillis()
)