package com.example.alfaqueso.domain.usecase

import com.example.alfaqueso.data.remote.dto.CuentasPorCobrarDto
import com.example.alfaqueso.domain.repository.CuentasPorCobrarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCuentasPendientesUseCase @Inject constructor(private val repository: CuentasPorCobrarRepository) {
    operator fun invoke(): Flow<List<CuentasPorCobrarDto>> {
        return repository.obtenerTodas().map { lista ->
            lista.filter { it.estado == "Pendiente" || it.estado == "Vencida" }
        }
    }
}