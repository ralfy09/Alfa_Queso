package com.example.alfaqueso.data.mapper

import com.example.alfaqueso.data.local.entities.PedidoEntity
import com.example.alfaqueso.data.remote.dto.PedidoDto

// De Entidad (Base de datos) a DTO (Pantalla)
fun PedidoEntity.toDto(): PedidoDto {
    return PedidoDto(
        pedidoId = pedidoId,
        clienteId = clienteId,
        nombreCliente = nombreCliente,
        fechaEntrega = fechaEntrega,
        notas = notas,
        estado = estado
    )
}

// De DTO (Pantalla) a Entidad (Base de datos)
fun PedidoDto.toEntity(): PedidoEntity {
    return PedidoEntity(
        pedidoId = pedidoId,
        clienteId = clienteId,
        nombreCliente = nombreCliente,
        fechaEntrega = fechaEntrega,
        notas = notas,
        estado = estado
    )
}