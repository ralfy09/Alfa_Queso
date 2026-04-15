package com.example.alfaqueso.domain.repository

// ESTE IMPORT ES VITAL
import com.example.alfaqueso.data.remote.dto.ClienteDto
import kotlinx.coroutines.flow.Flow

interface ClientesRepository {
    fun obtenerClientes(): Flow<List<ClienteDto>>
    // Asegúrate de que tenga el 'suspend'
    suspend fun insertarCliente(cliente: ClienteDto)
}