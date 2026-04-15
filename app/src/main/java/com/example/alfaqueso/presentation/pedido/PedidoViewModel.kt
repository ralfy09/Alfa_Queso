package com.example.alfaqueso.presentation.pedido

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaqueso.data.mapper.toDetallePedido
import com.example.alfaqueso.data.remote.dto.PedidoDto
import com.example.alfaqueso.domain.model.DetallePedido
import com.example.alfaqueso.domain.model.Pedido
import com.example.alfaqueso.domain.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PedidoViewModel : ViewModel() {

    private val _state = MutableStateFlow(PedidoUiState())
    val state: StateFlow<PedidoUiState> = _state.asStateFlow()

    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos.asStateFlow()

    private var todosLosPedidos = listOf<Pedido>()

    fun filterPedidos(query: String) {

        if (query.isBlank()) {
            _pedidos.value = todosLosPedidos
            return
        }

        val filtrados = todosLosPedidos.filter {
            it.clienteNombre.contains(query, ignoreCase = true)
        }

        _pedidos.value = filtrados
    }

    fun createPedido(pedidoDto: PedidoDto) {
        viewModelScope.launch {

            if (pedidoDto.nombreCliente.isBlank()) return@launch

            val nuevoPedido = Pedido(
                id = System.currentTimeMillis().toInt(),
                clienteNombre = pedidoDto.nombreCliente,
                productos = _state.value.productos,
                total = _state.value.total,
                fecha = System.currentTimeMillis(),
                estado = "Pendiente"
            )

            val nuevaLista = _pedidos.value + nuevoPedido

            _pedidos.value = nuevaLista
            todosLosPedidos = nuevaLista

            _state.value = PedidoUiState()
        }
    }

    fun onClienteChange(nombre: String) {
        _state.value = _state.value.copy(clienteNombre = nombre)
    }

    fun agregarProducto(producto: DetallePedido) {
        val nuevaLista = _state.value.productos + producto

        _state.value = _state.value.copy(
            productos = nuevaLista,
            total = nuevaLista.sumOf { it.subtotal }
        )
    }

    fun eliminarProducto(producto: DetallePedido) {
        val nuevaLista = _state.value.productos - producto

        _state.value = _state.value.copy(
            productos = nuevaLista,
            total = nuevaLista.sumOf { it.subtotal }
        )
    }

    fun agregarProductoDesdeProducto(producto: Producto, cantidad: Int = 1) {
        val detalle = producto.toDetallePedido(cantidad)

        val nuevaLista = _state.value.productos + detalle

        _state.value = _state.value.copy(
            productos = nuevaLista,
            total = nuevaLista.sumOf { it.subtotal }
        )
    }}