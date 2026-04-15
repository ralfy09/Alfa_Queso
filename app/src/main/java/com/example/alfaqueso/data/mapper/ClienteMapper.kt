package com.example.alfaqueso.data.mapper

import com.example.alfaqueso.data.local.entity.ClienteEntity
import com.example.alfaqueso.data.remote.dto.ClienteDto
import com.example.alfaqueso.data.mapper.toDto
import com.example.alfaqueso.data.mapper.toEntity


fun ClienteEntity.toDto(): ClienteDto {
    return ClienteDto(
        clienteId = this.clienteId,
        nombreNegocio = this.nombreNegocio,
        contacto = this.contacto,
        telefono = this.telefono,
        email = this.email,
        cuentasPorCobrar = emptyList()
    )
}

fun ClienteDto.toEntity(): ClienteEntity {
    return ClienteEntity(
        clienteId = this.clienteId,
        nombreNegocio = this.nombreNegocio,
        contacto = this.contacto,
        telefono = this.telefono,
        email = this.email
    )
}