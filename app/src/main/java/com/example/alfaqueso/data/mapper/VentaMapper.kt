package com.example.alfaqueso.data.mapper

import com.example.alfaqueso.data.local.entity.VentaEntity
import com.example.alfaqueso.data.remote.dto.VentaDto
import com.example.alfaqueso.domain.model.Venta
import java.text.SimpleDateFormat
import java.util.*


fun VentaEntity.toDomain(): Venta {
    return Venta(
        id = this.id,
        clienteId = this.clienteId,
        nombreCliente = this.nombreCliente,
        total = this.total,
        metodoPago = this.metodoPago,
        fecha = this.fecha
    )
}

fun Venta.toEntity(): VentaEntity {
    return VentaEntity(
        id = this.id,
        clienteId = this.clienteId,
        nombreCliente = this.nombreCliente,
        total = this.total,
        metodoPago = this.metodoPago,
        fecha = this.fecha
    )
}


fun Venta.toDto(): VentaDto {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return VentaDto(
        ventaId = this.id,
        clienteId = this.clienteId,
        nombreCliente = this.nombreCliente,
        total = this.total,
        metodoPago = this.metodoPago,
        fecha = sdf.format(Date(this.fecha))
    )
}

fun VentaDto.toDomain(): Venta {
    return Venta(
        id = this.ventaId,
        clienteId = this.clienteId,
        nombreCliente = this.nombreCliente,
        total = this.total,
        metodoPago = this.metodoPago,
        fecha = System.currentTimeMillis() // O parsear la fecha de Azure si lo prefieres
    )
}