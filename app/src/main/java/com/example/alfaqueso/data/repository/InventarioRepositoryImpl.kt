package com.example.alfaqueso.data.repository

import com.example.alfaqueso.data.local.dao.InventarioDao
import com.example.alfaqueso.data.local.entities.InventarioEntity
import com.example.alfaqueso.data.remote.dto.InventarioDto
import com.example.alfaqueso.domain.model.Inventario
import com.example.alfaqueso.domain.repository.InventarioRepository
import com.example.alfaqueso.data.mapper.toDomain
import com.example.alfaqueso.data.mapper.toEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class InventarioRepositoryImpl @Inject constructor(
    private val dao: InventarioDao
) : InventarioRepository {

    // 1. Obtener todos los productos del inventario
    override fun obtenerInventario(): Flow<List<Inventario>> {
        return dao.obtenerInventario()
            .map { entidades: List<InventarioEntity> ->
                entidades.map { it.toDomain() }
            }
            .flowOn(Dispatchers.IO)
    }

    // 2. Registrar un nuevo producto (implementando el método de la interfaz)
    override suspend fun registrarProducto(producto: InventarioDto) {
        val entidad = producto.toEntity()
        dao.insertarProducto(entidad)
    }

    // 3. Actualizar cantidad de un producto existente
    override suspend fun actualizarInventarioLocal(productoId: Int, nuevaCantidad: Int) {
        val producto = dao.obtenerProductoPorId(productoId)
        producto?.let {
            val productoActualizado = it.copy(stock = nuevaCantidad)
            dao.actualizarProducto(productoActualizado)
        }
    }

    // 4. Eliminar un producto del inventario
     suspend fun eliminarProducto(productoId: Int) {
        val producto = dao.obtenerProductoPorId(productoId)
        producto?.let {
            dao.eliminarProducto(it)
        }
    }

    // 5. Insertar múltiples productos (útil para datos iniciales)
     suspend fun insertarMultiplesProductos(productos: List<InventarioEntity>) {
        productos.forEach { producto ->
            dao.insertarProducto(producto)
        }
    }

    // 6. Obtener un producto por su ID
     suspend fun obtenerProductoPorId(productoId: Int): Inventario? {
        return dao.obtenerProductoPorId(productoId)?.toDomain()
    }

    // 7. Sincronizar inventario (implementando el método de la interfaz)
    override suspend fun sincronizarInventario() {
        // Como trabajas solo con base de datos local, esta función puede estar vacía
        // o puedes implementarla más tarde si necesitas sincronización con la nube
    }
}