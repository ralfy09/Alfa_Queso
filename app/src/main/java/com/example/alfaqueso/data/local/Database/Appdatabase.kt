package com.example.alfaqueso.data.local.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.alfaqueso.data.local.dao.*
import com.example.alfaqueso.data.local.entities.*
import com.example.alfaqueso.data.local.entity.*

@Database(
    entities = [
        InventarioEntity::class,
        VentaEntity::class,
        ClienteEntity::class,
        CuentasPorCobrarEntity::class,
        PedidoEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AlfaDatabase : RoomDatabase() {

    abstract val inventarioDao: InventarioDao
    abstract val ventaDao: VentaDao
    abstract val clienteDao: ClienteDao
    abstract val cxcDao: CuentasPorCobrarDao
    abstract val pedidoDao: PedidoDao
}