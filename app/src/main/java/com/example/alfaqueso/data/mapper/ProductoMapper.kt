package com.example.alfaqueso.data.mapper

import com.example.alfaqueso.data.local.entities.ProductoEntity
import com.example.alfaqueso.data.remote.dto.ProductoDto
import com.example.alfaqueso.domain.model.DetallePedido
import com.example.alfaqueso.domain.model.Producto

fun ProductoEntity.toDomain(): Producto {
    return Producto(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio,
        inventarioDisponible = this.inventarioDisponible,
        inventario = this.inventario
    )
}

fun ProductoDto.toEntity(): ProductoEntity {
    return ProductoEntity(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio,
        inventario = this.inventario,
        inventarioDisponible = this.inventario
    )
}

fun Producto.toEntity(): ProductoEntity{
    return ProductoEntity(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio,
        inventario = this.inventario,
        inventarioDisponible = this.inventario
    )
}

fun ProductoDto.toDomain(): Producto {
    return Producto(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio,
        inventario = this.inventario,
        inventarioDisponible = this.inventario
    )
}

fun Producto.toDto(): ProductoDto {
    return ProductoDto(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio,
        inventario = this.inventario
    )
}

fun Producto.toDetallePedido(cantidad: Int): DetallePedido {
    return DetallePedido(
        productoId = this.id,
        nombreProducto = this.nombre,
        cantidad = cantidad,
        precioUnitario = this.precio
    )
}