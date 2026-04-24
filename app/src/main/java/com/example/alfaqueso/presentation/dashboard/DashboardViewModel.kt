package com.example.alfaqueso.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaqueso.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val pedidosRepository: PedidosRepository,
    private val ventasRepository: VentasRepository,
    private val clientesRepository: ClientesRepository,
    private val inventarioRepository: InventarioRepository,
    private val productoRepository: ProductoRepository   // 👈 para el stock real
) : ViewModel() {

    val state: StateFlow<DashboardState> = combine(
        pedidosRepository.obtenerHistorialPedidos(),
        ventasRepository.obtenerHistorialVentas(),
        clientesRepository.obtenerClientes(),
        productoRepository.obtenerProductos()            // 👈 Flow de tabla_productos
    ) { pedidos, ventas, clientes, productos ->

        val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Funciona con "2026-04-24T14:13:07Z" y "2026-04-24 14:13"
        val ventasHoy = ventas.filter { venta ->
            venta.fecha.toString().startsWith(hoy)
        }

        DashboardState(
            ventasTotalesHistorico = ventas.sumOf { it.total },
            ventasTotalesHoy = ventasHoy.sumOf { it.total },
            cantidadVentasHoy = ventas.size,
            totalPedidos = pedidos.size,
            totalClientes = clientes.size,
            totalStock = productos.sumOf { it.inventarioDisponible }, // 👈 stock real
            cargando = false
        )
    }
        .catch { emit(DashboardState(cargando = false)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardState(cargando = true)
        )
}