package com.example.alfaqueso.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    usuarioId: Int? = null,
    onMenuItemClick: (String) -> Unit = {},
    onNavigateToPerfil: (Int) -> Unit = {},
    onLogoutClick: () -> Unit = {},
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedItem by remember { mutableStateOf("Dashboard") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val menuItems = listOf("Dashboard", "Ventas", "Pedido", "Cliente", "Inventario", "Compras", "Rutas", "Cuentas por Cobrar", "Cerrar Sesión")

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                Column(modifier = Modifier.fillMaxHeight().background(MaterialTheme.colorScheme.surface)) {
                    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary).padding(24.dp)) {
                        Text("Alfa Queso", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Sistema de Gestión", color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onNavigateToPerfil(usuarioId ?: 0) }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Usuario Admin", color = Color.White, fontWeight = FontWeight.Medium)
                        }
                    }
                    Column(modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState())) {
                        menuItems.forEach { item ->
                            NavigationDrawerItem(
                                label = { Text(item) },
                                selected = selectedItem == item,
                                onClick = {
                                    if (item == "Cerrar Sesión") showLogoutDialog = true
                                    else {
                                        selectedItem = item
                                        onMenuItemClick(item)
                                    }
                                    scope.launch { drawerState.close() }
                                },
                                icon = {
                                    Icon(
                                        imageVector = when (item) {
                                            "Dashboard" -> Icons.Default.Dashboard
                                            "Ventas" -> Icons.Default.Receipt
                                            "Pedido" -> Icons.Default.ShoppingCart
                                            "Cliente" -> Icons.Default.People
                                            "Inventario" -> Icons.Default.Inventory
                                            "Cerrar Sesión" -> Icons.AutoMirrored.Filled.ExitToApp
                                            else -> Icons.AutoMirrored.Filled.List
                                        },
                                        contentDescription = null,
                                        tint = if (item == "Cerrar Sesión") Color.Red else Color.Unspecified
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Alfa Queso", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                )
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
                Text("Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))

                // Las tarjetas ahora usan el 'state' que viene del ViewModel real
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SummaryCard("Ventas Hoy", "RD$ ${state.ventasTotalesHoy}", Color(0xFFE8F5E9), Color(0xFF4CAF50), Icons.Default.Payments, Modifier.weight(1f))
                    SummaryCard("Pedidos", "${state.totalPedidos}", Color(0xFFE3F2FD), Color(0xFF2196F3), Icons.Default.LocalShipping, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SummaryCard("Clientes", "${state.totalClientes}", Color(0xFFF3E5F5), Color(0xFF9C27B0), Icons.Default.Groups, Modifier.weight(1f))
                    SummaryCard("Stock", "${state.totalStock}", Color(0xFFFFF3E0), Color(0xFFFF9800), Icons.Default.Layers, Modifier.weight(1f))
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro de que deseas salir?") },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogoutClick() }) {
                    Text("Cerrar Sesión", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun SummaryCard(title: String, value: String, color: Color, iconColor: Color, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier.height(110.dp), colors = CardDefaults.cardColors(containerColor = color)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
            Icon(icon, contentDescription = null, tint = iconColor)
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}