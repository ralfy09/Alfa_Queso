package com.example.alfaqueso.data.repository

import com.example.alfaqueso.data.local.dao.ClienteDao
import com.example.alfaqueso.data.local.entity.ClienteEntity
import com.example.alfaqueso.data.remote.dto.ClienteDto
import com.example.alfaqueso.domain.repository.ClientesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ClientesRepositoryImpl @Inject constructor(
    private val dao: ClienteDao
) : ClientesRepository {

    override fun obtenerClientes(): Flow<List<ClienteDto>> {
        return dao.obtenerClientes().map { lista ->
            lista.map { it.toDto() }
        }
    }

    override suspend fun insertarCliente(cliente: ClienteDto) { // Agrega SUSPEND
        dao.insertarCliente(cliente.toEntity()) // toEntity() viene del mapper
    }
    }


fun ClienteEntity.toDto() = ClienteDto(clienteId, nombreNegocio, contacto, telefono, email, emptyList())
fun ClienteDto.toEntity() = ClienteEntity(clienteId, nombreNegocio, contacto, telefono, email)