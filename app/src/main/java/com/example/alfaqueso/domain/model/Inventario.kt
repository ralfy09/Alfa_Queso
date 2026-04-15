package com.example.alfaqueso.domain.model


data class Inventario(
    val id: Int = 0,
    val nombre: String,
    val stock: Int,
    val precio: Int
)