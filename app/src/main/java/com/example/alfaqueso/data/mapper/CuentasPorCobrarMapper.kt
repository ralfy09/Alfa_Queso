package com.example.alfaqueso.data.mapper

import com.example.alfaqueso.data.local.entity.CuentasPorCobrarEntity
import com.example.alfaqueso.data.remote.dto.CuentasPorCobrarDto

fun CuentasPorCobrarEntity.toDto(): CuentasPorCobrarDto {
    return CuentasPorCobrarDto(
        cuentaId = this.cuentaId,
        clienteId = this.clienteId,
        nombreNegocio = this.nombreNegocio,
        contacto = this.contacto,
        estado = this.estado,
        montoDeuda = this.montoDeuda,
        totalPorCobrar = this.totalPorCobrar,
        facturasVencidas = this.facturasVencidas
    )
}

fun CuentasPorCobrarDto.toEntity(): CuentasPorCobrarEntity {
    return CuentasPorCobrarEntity(
        cuentaId = this.cuentaId,
        clienteId = this.clienteId,
        nombreNegocio = this.nombreNegocio,
        contacto = this.contacto,
        montoDeuda = this.montoDeuda,
        totalPorCobrar = this.totalPorCobrar,
        estado = this.estado,
        facturasVencidas = this.facturasVencidas
    )
}