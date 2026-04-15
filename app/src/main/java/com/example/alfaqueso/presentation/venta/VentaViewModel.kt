package com.example.alfaqueso.presentation.venta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaqueso.data.remote.dto.VentaDto
import com.example.alfaqueso.domain.repository.VentasRepository
import com.example.alfaqueso.data.mapper.toDomain
import com.example.alfaqueso.data.mapper.toDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VentaViewModel @Inject constructor(
    private val repository: VentasRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<VentaUiState>(VentaUiState.Loading)
    val uiState: StateFlow<VentaUiState> = _uiState.asStateFlow()

    private val _operationState = MutableStateFlow<VentaOperationState>(VentaOperationState.Idle)
    val operationState: StateFlow<VentaOperationState> = _operationState.asStateFlow()

    // Variable temporal para guardar las ventas originales y poder filtrar
    private var todasLasVentas = emptyList<VentaDto>()

    init {
        cargarVentas()
    }

    private fun cargarVentas() {
        viewModelScope.launch {
            repository.obtenerHistorialVentas()
                .onStart { _uiState.value = VentaUiState.Loading }
                .catch { e -> _uiState.value = VentaUiState.Error(e.message ?: "Error desconocido") }
                .map { lista -> lista.map { it.toDto() } }
                .collect { lista ->
                    todasLasVentas = lista
                    actualizarLista(lista)
                }
        }
    }

    // SOLUCIÓN AL ERROR: filtrarVentas
    fun filtrarVentas(query: String) {
        val filtradas = if (query.isEmpty()) {
            todasLasVentas
        } else {
            todasLasVentas.filter { it.nombreCliente.contains(query, ignoreCase = true) }
        }
        actualizarLista(filtradas)
    }

    // SOLUCIÓN AL ERROR: registrarNuevaVenta
    fun registrarNuevaVenta(venta: VentaDto) {
        viewModelScope.launch {
            _operationState.value = VentaOperationState.Loading
            try {
                repository.registrarVenta(venta.toDomain())
                _operationState.value = VentaOperationState.Success("Venta registrada con éxito")
                // No hace falta llamar a cargarVentas si el repositorio usa Flow,
                // se actualizará solo.
            } catch (e: Exception) {
                _operationState.value = VentaOperationState.Error(e.message ?: "Error al guardar")
            }
        }
    }

    private fun actualizarLista(lista: List<VentaDto>) {
        _uiState.value = if (lista.isEmpty()) VentaUiState.Empty else VentaUiState.Success(lista)
    }

    fun resetOperationState() {
        _operationState.value = VentaOperationState.Idle
    }
}