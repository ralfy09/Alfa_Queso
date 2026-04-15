package com.example.alfaqueso.presentation.venta

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.alfaqueso.data.remote.dto.VentaDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentasScreen(
    viewModel: VentaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var searchText by remember { mutableStateOf("") }
    var showFormularioVenta by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Alfa Queso",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            "Ventas",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {

                    Text(
                        "Historial de Ventas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            viewModel.filtrarVentas(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar cliente...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!showFormularioVenta) {
                        Button(
                            onClick = { showFormularioVenta = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Nueva Venta")
                        }
                    }
                }
            }

            item {
                AnimatedVisibility(
                    visible = showFormularioVenta,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        FormularioNuevaVenta(
                            onGuardar = { nombre, total, metodo ->
                                val ventaDto = VentaDto(
                                    ventaId = 0,
                                    clienteId = 1,
                                    nombreCliente = nombre,
                                    total = total,
                                    metodoPago = metodo,
                                    fecha = System.currentTimeMillis().toString(),
                                    detalles = emptyList()
                                )
                                viewModel.registrarNuevaVenta(ventaDto)
                                showFormularioVenta = false
                            },
                            onCancelar = { showFormularioVenta = false }
                        )
                    }
                }
            }

            when (val estadoActual = uiState) {

                is VentaUiState.Loading -> {
                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                is VentaUiState.Success -> {
                    if (estadoActual.ventas.isEmpty()) {
                        item { EmptyView(searchText) }
                    } else {
                        items(estadoActual.ventas) { venta ->
                            VentaCard(venta)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                is VentaUiState.Error -> {
                    item {
                        Text(
                            "Error: ${estadoActual.message}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                is VentaUiState.Empty -> {
                    item { EmptyView(searchText) }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun FormularioNuevaVenta(
    onGuardar: (String, Double, String) -> Unit,
    onCancelar: () -> Unit
) {
    var clienteNombre by remember { mutableStateOf("") }
    var totalMonto by remember { mutableStateOf("") }
    var metodoPago by remember { mutableStateOf("Efectivo") }
    var showMetodoPagoDropdown by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                "Detalles de la Venta",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = clienteNombre,
                onValueChange = { clienteNombre = it },
                label = { Text("Nombre del Cliente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = totalMonto,
                onValueChange = { totalMonto = it },
                label = { Text("Monto Total (RD$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box {
                OutlinedTextField(
                    value = metodoPago,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Método de Pago") },
                    trailingIcon = {
                        IconButton(onClick = { showMetodoPagoDropdown = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                DropdownMenu(
                    expanded = showMetodoPagoDropdown,
                    onDismissRequest = { showMetodoPagoDropdown = false }
                ) {
                    listOf("Efectivo", "Transferencia", "Crédito").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                metodoPago = it
                                showMetodoPagoDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        val total = totalMonto.toDoubleOrNull() ?: 0.0
                        if (clienteNombre.isNotEmpty() && total > 0) {
                            onGuardar(clienteNombre, total, metodoPago)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}

@Composable
fun VentaCard(venta: VentaDto) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            Icon(
                Icons.Default.ReceiptLong,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(venta.nombreCliente, fontWeight = FontWeight.Bold)
                Text(
                    venta.metodoPago,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                "RD$ ${venta.total}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun EmptyView(searchText: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            Icons.Default.Inbox,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            if (searchText.isEmpty())
                "No hay ventas registradas"
            else
                "No se encontraron resultados",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}