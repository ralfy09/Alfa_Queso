package com.example.alfaqueso.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alfaqueso.presentation.Dashboard.DashboardScreen
import com.example.alfaqueso.presentation.cliente.ClienteScreen
import com.example.alfaqueso.presentation.inventario.InventarioScreen
import com.example.alfaqueso.presentation.login.LoginScreen
import com.example.alfaqueso.presentation.pedido.PedidosScreen
import com.example.alfaqueso.presentation.venta.VentasScreen

@Composable
fun AlfaQuesoNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route // La app siempre arranca en Login
    ) {
        // 1. Pantalla de Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // Al entrar, vamos al Dashboard y borramos el Login del historial
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Pantalla de Dashboard
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onMenuItemClick = { menu ->
                    // ¡Cambiamos el 'if' por un 'when' para que escuche todos los botones!
                    when (menu) {
                        "Inventario" -> navController.navigate(Screen.Inventario.route)
                        "Ventas" -> navController.navigate(Screen.Ventas.route)
                        "Cliente" -> navController.navigate(Screen.Cliente.route)
                        "Pedido" -> navController.navigate(Screen.Pedido.route)
                    }
                },
                onLogoutClick = {
                    // Al cerrar sesión, volvemos al Login
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        // 3. Pantalla de Inventario (La lista de quesos)
        composable(Screen.Inventario.route) {
            InventarioScreen()
        }

        // 4. Pantalla de Ventas
        composable(Screen.Ventas.route) {
            VentasScreen() // Hilt se encargará del resto adentro
        }
        // 5. pantalla de cliente
        composable(Screen.Cliente.route) {
            ClienteScreen(
                usuarioId = 0,
                onMenuItemClick = { menu ->
                    // ¡Cambiamos el 'if' por un 'when' para que escuche todos los botones!
                    when (menu) {
                        "Inventario" -> navController.navigate(Screen.Inventario.route)
                        "Ventas" -> navController.navigate(Screen.Ventas.route)
                        "Cliente" -> navController.navigate(Screen.Cliente.route)
                        "Pedido" -> navController.navigate(Screen.Pedido.route)
                    }
                }
            )

        }
        // 5. pantalla de cliente
        composable(Screen.Inventario.route) {
            InventarioScreen()
        }
        composable(Screen.Pedido.route) {
            PedidosScreen()
        }
    }
}
