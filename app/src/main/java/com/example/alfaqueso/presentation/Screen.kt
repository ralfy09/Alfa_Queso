package com.example.alfaqueso.presentation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard") // <--- Asegúrate que esto esté aquí
    object Inventario : Screen("inventario")
    object Ventas : Screen("ventas")
    object Cliente : Screen("cliente")
    object Pedido : Screen("pedido")
}