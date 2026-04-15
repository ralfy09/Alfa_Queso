package com.example.alfaqueso.domain.usecase

import com.example.alfaqueso.data.remote.dto.ClienteDto
import com.example.alfaqueso.domain.repository.ClientesRepository
import javax.inject.Inject

class AddClienteUseCase @Inject constructor(private val repository: ClientesRepository) {
    suspend operator fun invoke(cliente: ClienteDto) {
        if (cliente.nombreNegocio.isBlank()) throw Exception("El nombre del negocio es obligatorio")
        repository.insertarCliente(cliente)
    }
}