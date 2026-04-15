package com.example.alfaqueso.presentation.inventario

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.alfaqueso.data.remote.dto.InventarioDto
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.alfaqueso.domain.model.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioScreen(
    viewModel: InventarioViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle<InventarioUiState>(InventarioUiState.Loading)
    val operationState by viewModel.operationState.collectAsStateWithLifecycle<InventarioOperationState>(InventarioOperationState.Idle)

    var searchText by remember { mutableStateOf("") }
    var showFormularioProducto by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(operationState) {
        when (val state = operationState) {
            is InventarioOperationState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetOperationState()
                showFormularioProducto = false
            }
            is InventarioOperationState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetOperationState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("De Alfa Queso", color = MaterialTheme.colorScheme.onPrimary)
                        Text(
                            "Inventario",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
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
        ) {

            // 🔹 HEADER
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {

                Text(
                    "Gestión de Inventario",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        viewModel.filterProductos(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar producto...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showFormularioProducto = !showFormularioProducto },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (showFormularioProducto)
                            MaterialTheme.colorScheme.secondary
                        else
                            MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        if (showFormularioProducto) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (showFormularioProducto) "Cancelar" else "Nuevo Producto")
                }
            }

            // 🔹 CONTENIDO
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                if (showFormularioProducto) {
                    FormularioNuevoProducto(
                        onGuardar = { viewModel.createProducto(it) },
                        onCancelar = { showFormularioProducto = false },
                        isLoading = operationState is InventarioOperationState.Loading
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (val state = uiState) {

                    is InventarioUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    is InventarioUiState.Success -> {
                        state.productos.forEach { producto ->
                            ProductoCard(producto)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    is InventarioUiState.Empty -> {
                        Text(
                            "No hay productos",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    is InventarioUiState.Error -> {
                        Text(
                            "Error: ${state.message}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: Producto){
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Inventory2, contentDescription = null, tint = Color(0xFFFF9800), modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    "Stock: ${producto.inventarioDisponible} kg",
                    color = if(producto.inventarioDisponible < 10) Color.Red else Color.Gray
                )
            }
            Text("RD$ ${producto.precio}", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioNuevoProducto(
    onGuardar: (Producto) -> Unit,
    onCancelar: () -> Unit,
    isLoading: Boolean
) {
    var nombre by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Registrar Producto", fontWeight = FontWeight.Bold, color = Color(0xFFFF9800))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock (kg)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val s = stock.toIntOrNull() ?: 0
                    val p = precio.toIntOrNull() ?: 0
                    onGuardar(
                        Producto(
                            id = 0,
                            nombre = nombre,
                            inventario = s,
                            inventarioDisponible = s,
                            precio = p.toDouble()
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Guardar Producto")
                }
            }
        }
    }
}