package com.alfaqueso.domain.usecase

import com.example.alfaqueso.domain.model.Inventario // 👈 Cambiado a Inventario
import com.example.alfaqueso.domain.repository.InventarioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObtenerInventarioUseCase @Inject constructor(
    private val repository: InventarioRepository
) {
    operator fun invoke(): Flow<List<Inventario>> {
        return repository.obtenerInventario()
    }
}