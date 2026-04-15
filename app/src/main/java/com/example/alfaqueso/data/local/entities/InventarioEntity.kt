package com.example.alfaqueso.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventario")
data class InventarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val stock: Int,
    val precio: Int
)