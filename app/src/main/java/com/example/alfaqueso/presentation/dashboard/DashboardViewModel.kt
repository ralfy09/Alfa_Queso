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
    private val inventarioRepository: InventarioRepository
) : ViewModel() {

    val state: StateFlow<DashboardState> = combine(
        pedidosRepository.obtenerHistorialPedidos(),
        ventasRepository.obtenerHistorialVentas(),
        clientesRepository.obtenerClientes(),
        inventarioRepository.obtenerInventario()
    ) { pedidos, ventas, clientes, inventario ->

        val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val ventasHoy = ventas.filter { it.fecha.toString().contains(hoy) }

        DashboardState(
            totalPedidos = pedidos.size,
            ventasTotalesHoy = ventasHoy.sumOf { it.total },
            cantidadVentasHoy = ventasHoy.size,
            totalClientes = clientes.size,
            totalStock = inventario.sumOf { it.stock }, // 👈 Usamos 'stock' del mapper
            cargando = false
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardState(cargando = true)
        )

    }

