package com.example.alfaqueso.presentation.venta

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.alfaqueso.data.remote.dto.ClienteDto
import com.example.alfaqueso.presentation.Screen // Asegúrate de que esta ruta coincida con tu clase Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentasScreen(
    navController: NavController,
    viewModel: VentaViewModel = hiltViewModel()
) {
    // 1. Observamos los estados desde el ViewModel
    val clientes by viewModel.listaClientes.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var searchText by remember { mutableStateOf("") }
    var showFormularioVenta by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ventas", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Buscador y Botón de Nueva Venta
            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it; viewModel.filtrarVentas(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar venta...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showFormularioVenta = !showFormularioVenta },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (showFormularioVenta) "Cancelar" else "Nueva Venta")
                }
            }

            // Formulario Desplegable
            item {
                AnimatedVisibility(
                    visible = showFormularioVenta,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    FormularioNuevaVenta(
                        listaClientes = clientes,
                        onGuardar = { nombre, total, metodo ->
                            // Formateamos la fecha actual
                            val fechaActual = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

                            val nuevaVenta = com.example.alfaqueso.data.remote.dto.VentaDto(
                                ventaId = 0, // Se autogenera en la BD
                                clienteId = 0,
                                nombreCliente = nombre,
                                total = total,
                                metodoPago = metodo,
                                fecha = fechaActual
                            )

                            // Guardamos la venta
                            viewModel.registrarNuevaVenta(nuevaVenta)
                            showFormularioVenta = false

                            // Navegamos al Dashboard y limpiamos la pila
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Ventas.route) { inclusive = true }
                            }
                        },
                        onCancelar = { showFormularioVenta = false }
                    )
                }
            }

            // Separador visual
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Lista del Historial de Ventas según el estado
            when (val state = uiState) {
                is VentaUiState.Success -> {
                    items(state.ventas.size) { index ->
                        val venta = state.ventas[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            ListItem(
                                headlineContent = { Text(venta.nombreCliente, fontWeight = FontWeight.Bold) },
                                supportingContent = { Text("Total: $${venta.total} • ${venta.metodoPago}\nFecha: ${venta.fecha}") },
                                leadingContent = {
                                    Icon(
                                        Icons.Default.ShoppingCart,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                    }
                }
                is VentaUiState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }
                    }
                }
                is VentaUiState.Error -> {
                    item {
                        Text(
                            text = "Error al cargar las ventas: ${state.message}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                is VentaUiState.Empty -> {
                    item {
                        Text(
                            text = "No hay ventas registradas aún. ¡Registra la primera!",
                            modifier = Modifier.padding(16.dp),
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FormularioNuevaVenta(
    listaClientes: List<ClienteDto>,
    onGuardar: (String, Double, String) -> Unit,
    onCancelar: () -> Unit
) {
    var clienteNombre by remember { mutableStateOf("") }
    var producto by remember { mutableStateOf("") }
    var totalMonto by remember { mutableStateOf("") }
    var showClienteMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Registrar Nueva Venta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Selector de Cliente con anclaje corregido
            Text("Cliente", style = MaterialTheme.typography.labelLarge)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart)
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = clienteNombre,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Seleccionar cliente...") },
                    trailingIcon = {
                        IconButton(onClick = { showClienteMenu = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Desplegar")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showClienteMenu = true }
                )

                DropdownMenu(
                    expanded = showClienteMenu,
                    onDismissRequest = { showClienteMenu = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    if (listaClientes.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No hay clientes registrados") },
                            onClick = { showClienteMenu = false }
                        )
                    } else {
                        listaClientes.forEach { cliente ->
                            DropdownMenuItem(
                                text = { Text(cliente.nombreNegocio) },
                                onClick = {
                                    clienteNombre = cliente.nombreNegocio
                                    showClienteMenu = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = producto,
                onValueChange = { producto = it },
                label = { Text("Producto (Ej. Queso de Freír)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = totalMonto,
                onValueChange = { totalMonto = it },
                label = { Text("Total Cobrado") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        val montoFinal = totalMonto.toDoubleOrNull() ?: 0.0
                        if (clienteNombre.isNotBlank() && montoFinal > 0) {
                            onGuardar(clienteNombre, montoFinal, "Efectivo")
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }

                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}
