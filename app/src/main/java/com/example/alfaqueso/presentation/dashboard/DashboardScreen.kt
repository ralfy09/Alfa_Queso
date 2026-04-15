package com.example.alfaqueso.presentation.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    usuarioId: Int? = null,
    onMenuItemClick: (String) -> Unit = {},
    onNavigateToPerfil: (Int) -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var selectedItem by remember { mutableStateOf("Dashboard") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val menuItems = listOf(
        "Dashboard", "Ventas", "Pedido", "Cliente",
        "Inventario", "Compras", "Rutas", "Cuentas por Cobrar", "Cerrar Sesión"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    // Cabecera del Menú
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(24.dp)
                    ) {
                        Text(
                            "De Alfa Queso",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Sistema de Gestión",
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onNavigateToPerfil(usuarioId ?: 0) }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Usuario Admin", color = Color.White, fontWeight = FontWeight.Medium)
                        }
                    }

                    // Lista de Opciones
                    Column(modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState())) {
                        menuItems.forEach { item ->
                            NavigationDrawerItem(
                                label = { Text(item) },
                                selected = selectedItem == item,
                                onClick = {
                                    if (item == "Cerrar Sesión") {
                                        showLogoutDialog = true
                                    } else {
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
                                            "Clientes" -> Icons.Default.People
                                            "Inventario" -> Icons.Default.Inventory
                                            "Cerrar Sesión" -> Icons.Default.ExitToApp
                                            else -> Icons.Default.List
                                        },
                                        contentDescription = null,
                                        tint = if (item == "Cerrar Sesión") Color.Red else Color.Unspecified
                                    )
                                },
                                modifier = Modifier.padding(vertical = 2.dp)
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
                    title = {
                        Text(
                            "De Alfa Queso",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { paddingValues ->

            DashboardContent(
                selectedItem = selectedItem,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    "Cerrar Sesión",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    "¿Estás seguro de que deseas salir?",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogoutClick() }) {
                    Text(
                        "Cerrar Sesión",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun DashboardContent(selectedItem: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Dashboard",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            "Resumen general del negocio",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                icon = Icons.Default.Payments,
                title = "Ventas Hoy",
                value = "RD$ 45,230",
                color = Color(0xFFE8F5E9),
                iconColor = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                icon = Icons.Default.LocalShipping,
                title = "Pedidos",
                value = "28",
                color = Color(0xFFE3F2FD),
                iconColor = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                icon = Icons.Default.Groups,
                title = "Clientes",
                value = "156",
                color = Color(0xFFF3E5F5),
                iconColor = Color(0xFF9C27B0),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                icon = Icons.Default.Layers,
                title = "Stock",
                value = "342",
                color = Color(0xFFFFF3E0),
                iconColor = Color(0xFFFF9800),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SummaryCard(
    icon: ImageVector,
    title: String,
    value: String,
    color: Color,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(110.dp),
        colors = CardDefaults.cardColors(
            containerColor = color // 👈 color personalizado
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    DashboardScreen()
}