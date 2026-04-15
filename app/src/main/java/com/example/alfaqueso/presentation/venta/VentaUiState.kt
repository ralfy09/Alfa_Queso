package com.example.alfaqueso.presentation.venta

import com.example.alfaqueso.data.remote.dto.VentaDto

sealed class VentaUiState {
    object Loading : VentaUiState()
    object Empty : VentaUiState()
    data class Success(val ventas: List<VentaDto>) : VentaUiState()
    data class Error(val message: String) : VentaUiState()
}

sealed class VentaOperationState {
    object Idle : VentaOperationState()
    object Loading : VentaOperationState()
    data class Success(val message: String) : VentaOperationState()
    data class Error(val message: String) : VentaOperationState()
}