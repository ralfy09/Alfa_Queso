package com.example.alfaqueso.data.mapper


import com.example.alfaqueso.data.local.entities.InventarioEntity
import com.example.alfaqueso.data.remote.dto.InventarioDto
import com.example.alfaqueso.domain.model.Inventario

// Convierte de la Base de Datos al Modelo de Dominio
fun InventarioEntity.toDomain(): Inventario {
    return Inventario(
        id = id,
        nombre = nombre,
        stock = stock,
        precio = precio
    )
}

// Convierte del DTO (Interfaz) a la Base de Datos
fun InventarioDto.toDto(): InventarioEntity {
    return InventarioEntity(
        id = inventarioId,
        nombre = nombre,
        stock = stock,
        precio = precio
    )
}

// Convierte de la Base de Datos al DTO para la pantalla
fun InventarioEntity.toDto(): InventarioDto {
    return InventarioDto(
        inventarioId = id,
        nombre = nombre,
        stock = stock,
        precio = precio
    )
}