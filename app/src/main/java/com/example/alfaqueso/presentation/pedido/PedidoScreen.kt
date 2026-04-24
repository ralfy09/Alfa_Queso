package com.example.alfaqueso.presentation.pedido

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.alfaqueso.data.remote.dto.PedidoDto
import com.example.alfaqueso.data.remote.dto.VentaDto
import com.example.alfaqueso.presentation.venta.VentaUiState
import com.example.alfaqueso.presentation.venta.VentaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosScreen(
    navController: NavController,
    viewModel: PedidoViewModel = hiltViewModel(),
    ventaViewModel: VentaViewModel = hiltViewModel() // Cambiamos Cliente por Venta
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    // Observamos el estado de las ventas
    val ventasState by ventaViewModel.uiState.collectAsStateWithLifecycle()

    var searchText by remember { mutableStateOf("") }
    var showFormulario by remember { mutableStateOf(false) }

    // Extraemos la lista de ventas del estado
    val listaVentas = remember(ventasState) {
        if (ventasState is VentaUiState.Success) {
            (ventasState as VentaUiState.Success).ventas
        } else {
            emptyList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedidos Alfa Queso", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showFormulario = !showFormulario },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(if (showFormulario) Icons.Default.Close else Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Control de Entregas", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it; viewModel.filterPedidos(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar pedido...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                AnimatedVisibility(visible = showFormulario, enter = expandVertically(), exit = shrinkVertically()) {
                    FormularioPedido(
                        listaVentas = listaVentas, // Pasamos las ventas al formulario
                        onCrear = { pedidoDto ->
                            viewModel.createPedido(pedidoDto)
                            showFormulario = false
                        },
                        onCancelar = { showFormulario = false }
                    )
                }
            }

            item {
                Text("Historial Reciente", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
            }

            when (val state = uiState) {
                is PedidoUiState.Success -> {
                    items(state.pedidos) { pedido ->
                        PedidoCard(
                            pedido = pedido,
                            onEntregarClick = { viewModel.marcarComoEntregado(pedido.pedidoId) }
                        )
                    }
                }
                is PedidoUiState.Empty -> {
                    item { Text("No hay pedidos registrados.", color = Color.Gray, modifier = Modifier.padding(16.dp)) }
                }
                is PedidoUiState.Loading -> {
                    item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                }
                else -> {}
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun PedidoCard(pedido: PedidoDto, onEntregarClick: () -> Unit) {
    val esEntregado = pedido.estado == "Entregado"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (esEntregado) Color(0xFFE8F5E9) else Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(pedido.nombreCliente, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Entrega: ${pedido.fechaEntrega}", style = MaterialTheme.typography.bodySmall)
                }
                Surface(
                    color = if (esEntregado) Color(0xFF4CAF50) else Color(0xFFFFA000),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = pedido.estado,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 12.sp
                    )
                }
            }

            if (pedido.notas.isNotBlank()) {
                Text(
                    text = "Notas: ${pedido.notas}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color.Gray
                )
            }

            if (!esEntregado) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onEntregarClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Marcar como Entregado")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioPedido(
    listaVentas: List<VentaDto>, // Recibe lista de Ventas
    onCrear: (PedidoDto) -> Unit,
    onCancelar: () -> Unit
) {
    var ventaSeleccionada by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }
    var showVentaMenu by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))
                    }
                    showDatePicker = false
                }) { Text("Aceptar") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nuevo Pedido", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(12.dp))

            // Selector de Venta
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = ventaSeleccionada,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccionar Venta") },
                    modifier = Modifier.fillMaxWidth().clickable { showVentaMenu = true },
                    trailingIcon = { IconButton(onClick = { showVentaMenu = true }) { Icon(Icons.Default.ArrowDropDown, null) } }
                )
                DropdownMenu(expanded = showVentaMenu, onDismissRequest = { showVentaMenu = false }) {
                    if (listaVentas.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No hay ventas registradas") },
                            onClick = { showVentaMenu = false }
                        )
                    } else {
                        listaVentas.forEach { venta ->
                            DropdownMenuItem(
                                // Mostramos más detalles de la venta en el menú
                                text = { Text("${venta.nombreCliente} - $${venta.total}") },
                                onClick = {
                                    // Guardamos el nombre del cliente asociado a esa venta
                                    ventaSeleccionada = venta.nombreCliente
                                    showVentaMenu = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = fecha,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de Entrega") },
                modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                trailingIcon = { IconButton(onClick = { showDatePicker = true }) { Icon(Icons.Default.DateRange, null) } }
            )

            OutlinedTextField(
                value = notas,
                onValueChange = { notas = it },
                label = { Text("Notas Adicionales") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        if(ventaSeleccionada.isNotBlank() && fecha.isNotBlank()) {
                            onCrear(PedidoDto(nombreCliente = ventaSeleccionada, fechaEntrega = fecha, notas = notas))
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Guardar Pedido") }

                OutlinedButton(onClick = onCancelar, modifier = Modifier.weight(1f)) { Text("Cancelar") }
            }
        }
    }
}