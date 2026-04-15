package com.example.alfaqueso.di

import android.content.Context
import androidx.room.Room
import com.example.alfaqueso.domain.repository.VentasRepository
import com.example.alfaqueso.data.local.Database.AlfaDatabase
import com.example.alfaqueso.data.local.dao.ClienteDao
import com.example.alfaqueso.data.local.dao.CuentasPorCobrarDao
import com.example.alfaqueso.data.local.dao.ProductoDao
import com.example.alfaqueso.data.local.dao.VentaDao
import com.example.alfaqueso.data.remote.QuesoApi
import com.example.alfaqueso.data.repository.VentasRepositoryImpl
import com.example.alfaqueso.domain.repository.ClientesRepository
import com.example.alfaqueso.data.repository.ClientesRepositoryImpl
import com.example.alfaqueso.data.repository.CuentasPorCobrarRepositoryImpl
import com.example.alfaqueso.data.repository.InventarioRepositoryImpl
import com.example.alfaqueso.domain.repository.CuentasPorCobrarRepository
import com.example.alfaqueso.domain.repository.InventarioRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun proveerBaseDeDatos(@ApplicationContext context: Context): AlfaDatabase {
        return Room.databaseBuilder(context, AlfaDatabase::class.java, "alfa_queso_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun proveerProductoDao(db: AlfaDatabase): ProductoDao = db.productoDao

    @Provides
    @Singleton
    fun proveerVentaDao(db: AlfaDatabase): VentaDao = db.ventaDao

    @Provides
    @Singleton
    fun proveerInventarioRepository(
        api: QuesoApi,
        dao: ProductoDao
    ): InventarioRepository {
        return InventarioRepositoryImpl(api, dao)
    }

    @Provides
    @Singleton
    fun proveerVentasRepository(
        dao: VentaDao,
        api: QuesoApi
    ): VentasRepository {
        return VentasRepositoryImpl(dao, api)
    }

    @Provides
    @Singleton
    fun proveerClienteDao(db: AlfaDatabase): ClienteDao = db.clienteDao

    @Provides
    @Singleton
    fun proveerClientesRepository(dao: ClienteDao): ClientesRepository {
        return ClientesRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun proveerCuentasPorCobrarDao(db: AlfaDatabase): CuentasPorCobrarDao = db.cxcDao

    @Provides
    @Singleton
    fun proveerCuentasPorCobrarRepository(dao: CuentasPorCobrarDao): CuentasPorCobrarRepository {
        return CuentasPorCobrarRepositoryImpl(dao)
    }

    // HE BORRADO LA FUNCIÓN REPETIDA QUE ESTABA AQUÍ ABAJO
}