package com.example.alfaqueso.domain.usecase

import com.example.alfaqueso.data.remote.dto.ClienteDto
import com.example.alfaqueso.domain.repository.ClientesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetClientesUseCase @Inject constructor(private val repository: ClientesRepository) {
    operator fun invoke(query: String = ""): Flow<List<ClienteDto>> {
        return repository.obtenerClientes().map { lista ->
            if (query.isBlank()) lista
            else lista.filter { it.nombreNegocio.contains(query, ignoreCase = true) }
        }
    }
}