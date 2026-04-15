package com.example.alfaqueso.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_cxc")
data class CuentasPorCobrarEntity(
    @PrimaryKey(autoGenerate = true) val cuentaId: Int = 0,
    val clienteId: Int,
    val nombreNegocio: String,
    val contacto: String,
    val montoDeuda: Double,
    val totalPorCobrar: Double,
    val estado: String,
    val facturasVencidas: Int = 0,
    val fechaCreacion: Long = System.currentTimeMillis()
)