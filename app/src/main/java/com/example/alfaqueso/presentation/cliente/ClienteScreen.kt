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
    onNavigateToPerfil: (Int) -> Unit = {},
    onMenuItemClick: (String) -> Unit = {},
    viewModel: ClienteViewModel = hiltViewModel()
) {
    var selectedItem by remember { mutableStateOf("Dashboard") }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val operationState by viewModel.operationState.collectAsStateWithLifecycle()

    var searchText by remember { mutableStateOf("") }
    var showFormularioCliente by remember { mutableStateOf(false)
    }

    val menuItems = listOf(
        "Dashboard", "Ventas", "Pedido", "Cliente",
        "Inventario", "Compras", "Rutas", "Cuentas por Cobrar", "Cerrar Sesión"
    )

    val snackbarHostState = remember { SnackbarHostState() }

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
                        Column {
                            Text("De Alfa Queso", color = MaterialTheme.colorScheme.onPrimary)
                            Text(
                                "Clientes",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {

                // PANEL SUPERIOR
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Text(
                        "Directorio de clientes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            viewModel.filterClientes(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar cliente...") },
                        leadingIcon = { Icon(Icons.Default.Search, null) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showFormularioCliente = !showFormularioCliente },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            if (showFormularioCliente) Icons.Default.Close else Icons.Default.Add,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (showFormularioCliente) "Cancelar Registro"
                            else "Agregar Nuevo Cliente"
                        )
                    }
                }

                // FORMULARIO
                if (showFormularioCliente) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        FormularioNuevoCliente(
                            onGuardar = { viewModel.createCliente(it) },
                            onCancelar = { showFormularioCliente = false },
                            isLoading = operationState is ClienteOperationState.Loading
                        )
                    }
                }

                // LISTADO
                when (val state = uiState) {
                    is ClienteUiState.Loading -> {
                        Box(
                            Modifier.fillMaxWidth().padding(32.dp),
                            Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is ClienteUiState.Success -> {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            state.clientes.forEach { cliente ->
                                ClienteCard(cliente)
                            }
                        }
                    }

                    is ClienteUiState.Empty -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No se encontraron clientes")
                        }
                    }

                    is ClienteUiState.Error -> {
                        Text(
                            "Error: ${state.message}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClienteCard(cliente: ClienteDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                cliente.nombreNegocio,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Text("Contacto: ${cliente.contacto}")
            Text("Teléfono: ${cliente.telefono}")

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(onClick = { }) {
                Text("Ver Perfil", color = MaterialTheme.colorScheme.primary)
            }
        }
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                "Registrar Nuevo Cliente",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

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

                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading
                ) {
                    Text("Cancelar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (nombreNegocio.isNotBlank()) {
                            onGuardar(
                                ClienteDto(
                                    clienteId = 0,
                                    nombreNegocio = nombreNegocio,
                                    contacto = contacto,
                                    telefono = telefono,
                                    email = email
                                )
                            )
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading && nombreNegocio.isNotBlank()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}