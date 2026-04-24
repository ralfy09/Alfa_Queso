package com.example.alfaqueso.presentation.venta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaqueso.data.local.entity.ClienteEntity
import com.example.alfaqueso.data.mapper.toDomain
import com.example.alfaqueso.data.mapper.toDto
import com.example.alfaqueso.data.remote.dto.ClienteDto
import com.example.alfaqueso.data.remote.dto.VentaDto
import com.example.alfaqueso.domain.repository.ClientesRepository
import com.example.alfaqueso.domain.repository.VentasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VentaViewModel @Inject constructor(
    private val repository: VentasRepository,
    private val clientesRepository: ClientesRepository
) : ViewModel() {

    // CAMBIO AQUÍ: Simplificamos la lógica del Flow
    val listaClientes: StateFlow<List<ClienteDto>> = clientesRepository.obtenerClientes()
        .map { lista ->
            lista
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val ventas = repository.obtenerHistorialVentas()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    private val _uiState = MutableStateFlow<VentaUiState>(VentaUiState.Loading)
    val uiState: StateFlow<VentaUiState> = _uiState.asStateFlow()



    private val _operationState = MutableStateFlow<VentaOperationState>(VentaOperationState.Idle)
    val operationState: StateFlow<VentaOperationState> = _operationState.asStateFlow()


    private var todasLasVentas = emptyList<VentaDto>()

    init {
        cargarVentas()
    }

    private fun cargarVentas() {
        viewModelScope.launch {
            repository.obtenerHistorialVentas()
                .onStart { _uiState.value = VentaUiState.Loading }
                .catch { e -> _uiState.value = VentaUiState.Error(e.message ?: "Error") }
                .map { lista -> lista.map { it.toDto() } }
                .collect { lista ->
                    todasLasVentas = lista
                    actualizarLista(lista)
                }
        }
    }


    fun filtrarVentas(query: String) {
        val filtradas = if (query.isEmpty()) todasLasVentas else todasLasVentas.filter { it.nombreCliente.contains(query, ignoreCase = true) }
        actualizarLista(filtradas)
    }
    fun registrarNuevaVenta(venta: VentaDto) {
        viewModelScope.launch {
            try {
                repository.registrarVenta(venta.toDomain())


                cargarVentas()

                _operationState.value = VentaOperationState.Success("Venta registrada")
            } catch (e: Exception) {
                _operationState.value = VentaOperationState.Error(e.message ?: "Error")
            }
        }
    }

    private fun actualizarLista(lista: List<VentaDto>) {
        _uiState.value = if (lista.isEmpty()) VentaUiState.Empty else VentaUiState.Success(lista)
    }

    fun resetOperationState() { _operationState.value = VentaOperationState.Idle }
}