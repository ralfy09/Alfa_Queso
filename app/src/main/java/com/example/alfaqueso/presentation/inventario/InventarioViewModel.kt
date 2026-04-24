package com.example.alfaqueso.presentation.inventario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaqueso.domain.model.Producto
import com.example.alfaqueso.domain.usecase.ObtenerProductosUseCase
import com.example.alfaqueso.domain.usecase.UpSertProductoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventarioViewModel @Inject constructor(
    private val upSertProductoUseCase: UpSertProductoUseCase,
    private val obtenerProductosUseCase: ObtenerProductosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<InventarioUiState>(InventarioUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _operationState = MutableStateFlow<InventarioOperationState>(InventarioOperationState.Idle)
    val operationState = _operationState.asStateFlow()

    private var todosLosProductos: List<Producto> = emptyList()

    init {
        loadProductos()
    }

    fun loadProductos() {
        viewModelScope.launch {
            obtenerProductosUseCase()
                .onStart {
                    _uiState.value = InventarioUiState.Loading
                }
                .catch { e ->
                    _uiState.value = InventarioUiState.Error(
                        e.message ?: "Error desconocido"
                    )
                }
                .collect { lista: List<Producto> ->
                    todosLosProductos = lista
                    _uiState.value = if (lista.isEmpty()) {
                        InventarioUiState.Empty
                    } else {
                        InventarioUiState.Success(lista)
                    }
                }
        }
    }

    fun filterProductos(query: String) {
        val filtrados: List<Producto> = if (query.isEmpty()) {
            todosLosProductos
        } else {
            todosLosProductos.filter { it.nombre.contains(query, ignoreCase = true) }
        }
        _uiState.value = if (filtrados.isEmpty()) {
            InventarioUiState.Empty
        } else {
            InventarioUiState.Success(filtrados)
        }
    }

    fun createProducto(producto: Producto) {
        viewModelScope.launch {
            _operationState.value = InventarioOperationState.Loading
            try {
                upSertProductoUseCase(producto)
                _operationState.value = InventarioOperationState.Success("Producto guardado con éxito")
            } catch (e: Exception) {
                _operationState.value = InventarioOperationState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun resetOperationState() {
        _operationState.value = InventarioOperationState.Idle
    }
}