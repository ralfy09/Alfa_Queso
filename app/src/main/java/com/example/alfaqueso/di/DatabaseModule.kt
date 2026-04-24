package com.example.alfaqueso.di

import android.content.Context
import androidx.room.Room
import com.example.alfaqueso.data.local.Database.AlfaDatabase
import com.example.alfaqueso.data.local.dao.*
import com.example.alfaqueso.data.remote.QuesoApi
import com.example.alfaqueso.data.repository.*
import com.example.alfaqueso.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // 🔹 DATABASE
    @Provides
    @Singleton
    fun proveerBaseDeDatos(@ApplicationContext context: Context): AlfaDatabase {
        return Room.databaseBuilder(
            context,
            AlfaDatabase::class.java,
            "alfa_queso_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    // 🔹 DAOs
    @Provides
    fun providePedidoDao(db: AlfaDatabase): PedidoDao = db.pedidoDao

    @Provides
    fun provideInventarioDao(db: AlfaDatabase): InventarioDao = db.inventarioDao

    @Provides
    fun provideVentaDao(db: AlfaDatabase): VentaDao = db.ventaDao

    @Provides
    fun provideClienteDao(db: AlfaDatabase): ClienteDao = db.clienteDao

    @Provides
    fun provideCxcDao(db: AlfaDatabase): CuentasPorCobrarDao = db.cxcDao

    @Provides
    fun provideProductoDao(db: AlfaDatabase): ProductoDao = db.productoDao

    // 🔹 REPOSITORIES
    @Provides
    @Singleton
    fun provideInventarioRepository(
        dao: InventarioDao
    ): InventarioRepository = InventarioRepositoryImpl(dao)

    @Provides
    @Singleton
    fun providePedidosRepository(
        dao: PedidoDao
    ): PedidosRepository = PedidosRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideVentasRepository(
        dao: VentaDao,
        api: QuesoApi
    ): VentasRepository = VentasRepositoryImpl(dao, api)

    @Provides
    @Singleton
    fun provideClientesRepository(
        dao: ClienteDao
    ): ClientesRepository = ClientesRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideCxcRepository(
        dao: CuentasPorCobrarDao
    ): CuentasPorCobrarRepository = CuentasPorCobrarRepositoryImpl(dao)
}