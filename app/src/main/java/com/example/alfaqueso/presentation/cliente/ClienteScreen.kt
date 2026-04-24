package com.example.alfaqueso.presentation.cliente

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.alfaqueso.data.remote.dto.ClienteDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteScreen(
    usuarioId: Int? = null,
    onNavigateToPerfil: (Int) -> Unit = {},
    onMenuItemClick: (String) -> Unit = {},
    viewModel: ClienteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val operationState by viewModel.operationState.collectAsStateWithLifecycle()

    var searchText by remember { mutableStateOf("") }
    var showFormularioCliente by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(operationState) {
        if (operationState is ClienteOperationState.Success) {
            snackbarHostState.showSnackbar("Cliente guardado correctamente")
            viewModel.resetOperationState()
            showFormularioCliente = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("De Alfa Queso", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { /* Abrir Drawer si lo necesitas */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Notificaciones */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {


            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Clientes", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text("Directorio de clientes", color = Color.Gray, fontSize = 16.sp)
                    }

                    Button(
                        onClick = { showFormularioCliente = !showFormularioCliente },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(if (showFormularioCliente) Icons.Default.Close else Icons.Default.Add, contentDescription = "Nuevo", tint = Color.White)
                    }
                }
            }

            // 2. Buscador redondeado
            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it; viewModel.filterClientes(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar cliente...") },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                AnimatedVisibility(
                    visible = showFormularioCliente,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    FormularioNuevoCliente(
                        onGuardar = { viewModel.createCliente(it) },
                        onCancelar = { showFormularioCliente = false },
                        isLoading = operationState is ClienteOperationState.Loading
                    )
                }
            }

            if (!showFormularioCliente && uiState is ClienteUiState.Success) {
                items((uiState as ClienteUiState.Success).clientes.size) { index ->
                    ClienteCard((uiState as ClienteUiState.Success).clientes[index])
                }
            }
        }
    }
}

@Composable
fun FormularioNuevoCliente(
    onGuardar: (ClienteDto) -> Unit,
    onCancelar: () -> Unit,
    isLoading: Boolean
) {
    var nombreNegocio by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Nuevo Cliente", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Nombre del Negocio", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = nombreNegocio,
                onValueChange = { nombreNegocio = it },
                placeholder = { Text("Ej: Supermercado La Central", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Contacto", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = contacto,
                onValueChange = { contacto = it },
                placeholder = { Text("Ej: Juan Pérez", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Teléfono", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it },
                        placeholder = { Text("555-1234", color = Color.LightGray) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Email", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("email@ejemplo.com", color = Color.LightGray) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        if (nombreNegocio.isNotBlank()) {
                            onGuardar(ClienteDto(0, nombreNegocio, contacto, telefono, email))
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    Text(if (isLoading) "Guardando..." else "Guardar", color = Color.White)
                }

                Button(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0F0F0)), // Gris claro
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar", color = Color.DarkGray)
                }
            }
        }
    }
}

@Composable
fun ClienteCard(cliente: ClienteDto) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(cliente.nombreNegocio, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Contacto: ${if (cliente.contacto.isNotBlank()) cliente.contacto else "N/A"}", color = Color.Gray)
            Text("Tel: ${cliente.telefono}", color = Color.Gray)
        }
    }
}