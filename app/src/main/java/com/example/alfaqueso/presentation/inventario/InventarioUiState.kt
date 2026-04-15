package com.example.alfaqueso.presentation.inventario

import com.example.alfaqueso.domain.model.Producto


//data class InventarioUIstate(
//    val isLoading: Boolean = false,
//    val productos: List<com.example.alfaqueso.data.remote.dto.InventarioDto> = emptyList(),
//    val error: String? = null
//)

sealed class InventarioUiState {
    object Loading : InventarioUiState()
    object Empty : InventarioUiState()
    data class Success(val productos: List<Producto>) : InventarioUiState()
    data class Error(val message: String) : InventarioUiState()
}

sealed class InventarioOperationState {
    object Idle : InventarioOperationState()
    object Loading : InventarioOperationState()
    data class Success(val message: String) : InventarioOperationState()
    data class Error(val message: String) : InventarioOperationState()
}