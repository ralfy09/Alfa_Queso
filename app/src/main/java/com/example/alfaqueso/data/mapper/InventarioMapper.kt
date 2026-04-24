package com.example.alfaqueso.data.mapper

import com.example.alfaqueso.data.local.entities.InventarioEntity
import com.example.alfaqueso.data.remote.dto.InventarioDto
import com.example.alfaqueso.domain.model.Inventario

fun InventarioEntity.toDomain(): Inventario {
    return Inventario(
        id = id,
        nombre = nombre,
        stock = stock,
        precio = precio
    )
}

fun Inventario.toEntity(): InventarioEntity {
    return InventarioEntity(
        id = id,
        nombre = nombre,
        stock = stock,
        precio = precio
    )
}

fun InventarioDto.toEntity(): InventarioEntity {
    return InventarioEntity(
        id = inventarioId,
        nombre = nombre,
        stock = stock,
        precio = precio
    )
}
