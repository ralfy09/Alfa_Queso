package com.example.alfaqueso.presentation.cliente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaqueso.data.remote.dto.ClienteDto
import com.example.alfaqueso.domain.repository.ClientesRepository
import com.example.alfaqueso.presentation.cliente.ClienteOperationState
import com.example.alfaqueso.presentation.cliente.ClienteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClienteViewModel @Inject constructor(
    private val repository: ClientesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ClienteUiState>(ClienteUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _operationState = MutableStateFlow<ClienteOperationState>(ClienteOperationState.Idle)
    val operationState = _operationState.asStateFlow()

    private var todosLosClientes = emptyList<ClienteDto>()

    init {
        loadClientes()
    }

    fun loadClientes() {
        viewModelScope.launch {
            _uiState.value = ClienteUiState.Loading
            repository.obtenerClientes().collect { clientes ->
                todosLosClientes = clientes
                _uiState.value = if (clientes.isEmpty()) ClienteUiState.Empty else ClienteUiState.Success(clientes)
            }
        }
    }

    fun createCliente(cliente: ClienteDto) {
        viewModelScope.launch {
            _operationState.value = ClienteOperationState.Loading
            try {
                repository.insertarCliente(cliente)
                _operationState.value = ClienteOperationState.Success("Cliente guardado correctamente")
                loadClientes() // Recargamos la lista
            } catch (e: Exception) {
                _operationState.value = ClienteOperationState.Error("Error: ${e.message}")
            }
        }
    }

    fun filterClientes(query: String) {
        val filtrados = todosLosClientes.filter {
            it.nombreNegocio.contains(query, ignoreCase = true) ||
                    it.contacto.contains(query, ignoreCase = true)
        }
        _uiState.value = if (filtrados.isEmpty()) ClienteUiState.Empty else ClienteUiState.Success(filtrados)
    }

    fun resetOperationState() {
        _operationState.value = ClienteOperationState.Idle
    }
}