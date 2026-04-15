package com.example.alfaqueso.presentation.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import com.example.alfaqueso.data.remote.dto.ClienteDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteScreen(
    usuarioId: Int? = null,
    onMenuItemClick: (String) -> Unit = {},
    onNavigateToPerfil: (Int) -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    viewModel: ClienteViewModel = hiltViewModel()
) {
    var selectedItem by remember { mutableStateOf("Clientes") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val operationState by viewModel.operationState.collectAsStateWithLifecycle()

    var searchText by remember { mutableStateOf("") }
    var showFormularioCliente by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    val menuItems = listOf(
        "Dashboard", "Ventas", "Pedidos", "Cliente",
        "Inventario", "Compras", "Rutas", "Cuentas por Cobrar", "Cerrar Sesión"
    )

    // Efecto para manejar mensajes de éxito o error al guardar
    LaunchedEffect(operationState) {
        when (val state = operationState) {
            is ClienteOperationState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetOperationState()
                showFormularioCliente = false
            }
            is ClienteOperationState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetOperationState()
            }
            else -> {}
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = Color.White
            ) {
                // Cabecera del Drawer
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFF9800))
                        .padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFFF9800))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Usuario Admin", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("admin@alfaqueso.com", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Items del Menú
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
                                imageVector = when(item) {
                                    "Dashboard" -> Icons.Default.Dashboard
                                    "Ventas" -> Icons.Default.Receipt
                                    "Clientes" -> Icons.Default.Groups
                                    "Inventario" -> Icons.Default.Inventory
                                    "Cerrar Sesión" -> Icons.Default.ExitToApp
                                    else -> Icons.Default.List
                                },
                                contentDescription = null,
                                tint = if (item == "Cerrar Sesión") Color.Red else Color.Unspecified
                            )
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("De Alfa Queso", color = Color.White)
                            Text("Clientes", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFF9800))
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Panel Superior: Buscador y Botón Nuevo
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Text("Directorio de clientes", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            viewModel.filterClientes(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar cliente...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showFormularioCliente = !showFormularioCliente },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                    ) {
                        Icon(if (showFormularioCliente) Icons.Default.Close else Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (showFormularioCliente) "Cancelar Registro" else "Agregar Nuevo Cliente")
                    }
                }

                // Mostrar Formulario si está activo
                if (showFormularioCliente) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        FormularioNuevoCliente(
                            onGuardar = { viewModel.createCliente(it) },
                            onCancelar = { showFormularioCliente = false },
                            isLoading = operationState is ClienteOperationState.Loading
                        )
                    }
                }

                // Contenido principal según el estado de la lista
                when (val state = uiState) {
                    is ClienteUiState.Loading -> {
                        Box(Modifier.fillMaxWidth().padding(32.dp), Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFFFF9800))
                        }
                    }
                    is ClienteUiState.Success -> {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            state.clientes.forEach { cliente ->
                                ClienteCard(cliente = cliente, onVerDetalles = { /* Navegar */ })
                            }
                        }
                    }
                    is ClienteUiState.Empty -> {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(64.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.PeopleOutline, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("No se encontraron clientes", color = Color.Gray)
                        }
                    }
                    is ClienteUiState.Error -> {
                        Text("Error: ${state.message}", color = Color.Red, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }

    // Diálogo de Cerrar Sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro de que deseas salir del sistema?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogoutClick()
                }) { Text("Cerrar Sesión", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Volver") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioNuevoCliente(
    onGuardar: (ClienteDto) -> Unit,
    onCancelar: () -> Unit,
    isLoading: Boolean = false
) {
    var nombreNegocio by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Registrar Nuevo Cliente", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF9800))
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombreNegocio,
                onValueChange = { nombreNegocio = it },
                label = { Text("Nombre del Negocio") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = contacto,
                onValueChange = { contacto = it },
                label = { Text("Persona de Contacto") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onCancelar, modifier = Modifier.weight(1f), enabled = !isLoading) {
                    Text("Cancelar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (nombreNegocio.isNotBlank()) {
                            onGuardar(ClienteDto(0, nombreNegocio, contacto, telefono, email, emptyList()))
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                    enabled = !isLoading && nombreNegocio.isNotBlank()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

@Composable
fun ClienteCard(cliente: ClienteDto, onVerDetalles: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(cliente.nombreNegocio, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFFF9800))
            Text("Persona de contacto: ${cliente.contacto}", fontSize = 14.sp)
            Text("Teléfono: ${cliente.telefono}", fontSize = 14.sp, color = Color.Gray)
            if (cliente.email.isNotEmpty()) {
                Text("Email: ${cliente.email}", fontSize = 14.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onVerDetalles,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Perfil y Cuentas", color = Color(0xFFFF9800))
            }
        }
    }
}