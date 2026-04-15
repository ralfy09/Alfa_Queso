package com.example.alfaqueso.presentation.pedido

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alfaqueso.data.remote.dto.PedidoDto
import com.example.alfaqueso.presentation.cliente.ClienteUiState
import com.example.alfaqueso.presentation.cliente.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosScreen(
    viewModel: PedidoViewModel = hiltViewModel(),
    clienteViewModel: ClienteViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val clientesState by clienteViewModel.uiState.collectAsStateWithLifecycle()

    var searchText by remember { mutableStateOf("") }
    var showFormulario by remember { mutableStateOf(false) }

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
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Menu, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showFormulario = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Pedido")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {

            // Título
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Pedidos",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    "Gestión de pedidos",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Resumen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ResumenCard("Pendientes", "1", "⏳", Modifier.weight(1f))
                ResumenCard("En Ruta", "2", "🚚", Modifier.weight(1f))
            }

            // Buscador
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            viewModel.filterPedidos(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar pedido...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        }
                    )
                }
            }

            if (showFormulario) {
                FormularioPedido(
                    clientesState = clientesState,
                    onCrear = { pedidoDto ->
                        viewModel.createPedido(pedidoDto)
                        showFormulario = false
                    },
                    onCancelar = { showFormulario = false }
                )
            }
        }
    }
}

@Composable
fun ResumenCard(
    titulo: String,
    cantidad: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Column {
                Text(
                    titulo,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    cantidad,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(icon, modifier = Modifier.align(Alignment.CenterEnd))
        }
    }
}

@Composable
fun FormularioPedido(
    clientesState: ClienteUiState,
    onCrear: (PedidoDto) -> Unit,
    onCancelar: () -> Unit
) {
    var clienteSeleccionado by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                "Nuevo Pedido",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = clienteSeleccionado,
                onValueChange = { clienteSeleccionado = it },
                label = { Text("Cliente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha de Entrega") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notas,
                onValueChange = { notas = it },
                label = { Text("Notas") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                Button(
                    onClick = {
                        val pedido = PedidoDto(
                            nombreCliente = clienteSeleccionado,
                            fechaEntrega = fecha,
                            notas = notas
                        )
                        onCrear(pedido)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Crear")
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