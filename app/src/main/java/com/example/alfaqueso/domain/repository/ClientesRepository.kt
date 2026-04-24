package com.example.alfaqueso.domain.repository


import com.example.alfaqueso.data.remote.dto.ClienteDto
import kotlinx.coroutines.flow.Flow

interface ClientesRepository {
    fun obtenerClientes(): Flow<List<ClienteDto>>
    suspend fun insertarCliente(cliente: ClienteDto)
}