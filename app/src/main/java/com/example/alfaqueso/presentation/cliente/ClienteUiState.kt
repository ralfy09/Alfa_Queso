package com.example.alfaqueso.presentation.cliente

import com.example.alfaqueso.data.remote.dto.ClienteDto

sealed class ClienteUiState {
    object Loading : ClienteUiState()
    data class Success(val clientes: List<ClienteDto>) : ClienteUiState()
    data class Error(val message: String) : ClienteUiState()
    object Empty : ClienteUiState()
}

sealed class ClienteOperationState {
    object Idle : ClienteOperationState()
    object Loading : ClienteOperationState()
    data class Success(val message: String) : ClienteOperationState()
    data class Error(val message: String) : ClienteOperationState()
}