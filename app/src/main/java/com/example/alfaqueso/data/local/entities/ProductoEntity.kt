package com.example.alfaqueso.data.local.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_productos")
data class ProductoEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val precio: Double,
    val inventario: Int,
    val inventarioDisponible: Int,
    val ultimaActualizacion: Long = System.currentTimeMillis()
)