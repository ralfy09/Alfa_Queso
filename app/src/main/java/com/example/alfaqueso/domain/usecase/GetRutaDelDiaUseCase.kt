package com.example.alfaqueso.domain.usecase

import com.example.alfaqueso.data.remote.dto.ClienteDto
import com.example.alfaqueso.domain.repository.ClientesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRutaDelDiaUseCase @Inject constructor(private val clientesRepo: ClientesRepository) {
    operator fun invoke(diaSemana: String): Flow<List<ClienteDto>> {
        return clientesRepo.obtenerClientes().map { lista ->
            lista.filter { it.cuentasPorCobrar.isNotEmpty() }
        }
    }
}