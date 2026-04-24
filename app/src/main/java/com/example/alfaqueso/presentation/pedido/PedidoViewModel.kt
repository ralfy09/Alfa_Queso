package com.example.alfaqueso.presentation.pedido

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaqueso.data.mapper.toDetallePedido
import com.example.alfaqueso.data.remote.dto.PedidoDto
import com.example.alfaqueso.domain.model.DetallePedido
import com.example.alfaqueso.domain.model.Producto
import com.example.alfaqueso.domain.repository.PedidosRepository // ¡El puente a la Base de Datos!
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PedidoViewModel @Inject constructor(
    private val repository: PedidosRepository
) : ViewModel() {

    private val _state = MutableStateFlow<PedidoUiState>(PedidoUiState.Loading)
    val state: StateFlow<PedidoUiState> = _state.asStateFlow()

    private val _formularioState = MutableStateFlow(FormularioPedidoState())
    val formularioState: StateFlow<FormularioPedidoState> = _formularioState.asStateFlow()

    private var todosLosPedidos = listOf<PedidoDto>()

    init {
        // Al iniciar la pantalla, leemos todos los pedidos guardados en la Base de Datos
        viewModelScope.launch {
            repository.obtenerHistorialPedidos().collect { listaPedidos ->
                todosLosPedidos = listaPedidos
                actualizarPantalla(listaPedidos)
            }
        }
    }

    fun filterPedidos(query: String) {
        if (query.isBlank()) {
            actualizarPantalla(todosLosPedidos)
            return
        }
        val filtrados = todosLosPedidos.filter {
            it.nombreCliente.contains(query, ignoreCase = true)
        }
        actualizarPantalla(filtrados)
    }

    fun createPedido(pedidoDto: PedidoDto) {
        viewModelScope.launch {
            if (pedidoDto.nombreCliente.isBlank()) return@launch

            // ¡LA MAGIA DEL ID! Generamos un ID único y le ponemos estado "Pendiente"
            val pedidoAguardar = pedidoDto.copy(
                pedidoId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                estado = "Pendiente"
            )

            // Guardamos el pedido permanentemente en Room
            repository.registrarPedido(pedidoAguardar)

            // Limpiamos el formulario para el siguiente
            _formularioState.value = FormularioPedidoState()
        }
    }

    fun marcarComoEntregado(idDelPedido: Int) {
        viewModelScope.launch {
            // Le pedimos a Room que actualice este pedido específico a "Entregado"
            repository.actualizarEstadoPedido(idDelPedido, "Entregado")
        }
    }

    private fun actualizarPantalla(lista: List<PedidoDto>) {
        if (lista.isEmpty()) {
            _state.value = PedidoUiState.Empty
        } else {
            _state.value = PedidoUiState.Success(lista)
        }
    }

    // =========================================================
    // TUS FUNCIONES DE PRODUCTOS PARA EL DETALLE DEL PEDIDO
    // =========================================================

    fun onClienteChange(nombre: String) {
        _formularioState.value = _formularioState.value.copy(clienteNombre = nombre)
    }

    fun agregarProducto(producto: DetallePedido) {
        val nuevaLista = _formularioState.value.productos + producto
        _formularioState.value = _formularioState.value.copy(
            productos = nuevaLista,
            total = nuevaLista.sumOf { it.subtotal }
        )
    }

    fun eliminarProducto(producto: DetallePedido) {
        val nuevaLista = _formularioState.value.productos - producto
        _formularioState.value = _formularioState.value.copy(
            productos = nuevaLista,
            total = nuevaLista.sumOf { it.subtotal }
        )
    }

    fun agregarProductoDesdeProducto(producto: Producto, cantidad: Int = 1) {
        val detalle = producto.toDetallePedido(cantidad)
        val nuevaLista = _formularioState.value.productos + detalle
        _formularioState.value = _formularioState.value.copy(
            productos = nuevaLista,
            total = nuevaLista.sumOf { it.subtotal }
        )
    }
}