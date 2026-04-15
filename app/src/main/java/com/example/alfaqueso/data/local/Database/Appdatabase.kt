package com.example.alfaqueso.data.local.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.alfaqueso.data.local.dao.ClienteDao
import com.example.alfaqueso.data.local.dao.CuentasPorCobrarDao
import com.example.alfaqueso.data.local.dao.ProductoDao
import com.example.alfaqueso.data.local.dao.VentaDao
import com.example.alfaqueso.data.local.entities.ProductoEntity
import com.example.alfaqueso.data.local.entity.ClienteEntity
import com.example.alfaqueso.data.local.entity.CuentasPorCobrarEntity
import com.example.alfaqueso.data.local.entity.VentaEntity
@Database(
    entities = [
        ProductoEntity::class,
        VentaEntity::class,
        ClienteEntity::class,
        CuentasPorCobrarEntity::class
    ],
    version = 4, // <--- Sube a 4
    exportSchema = false
)
abstract class AlfaDatabase : RoomDatabase() {
    abstract val productoDao: ProductoDao
    abstract val ventaDao: VentaDao
    abstract val clienteDao: ClienteDao
    abstract val cxcDao: CuentasPorCobrarDao
}