package com.example.alfaqueso.data.remote

import com.example.alfaqueso.data.remote.dto.LoginRequest
import com.example.alfaqueso.data.remote.dto.LoginResponse
import com.example.alfaqueso.data.remote.dto.ProductoDto
import com.example.alfaqueso.data.remote.dto.VentaDto // Necesitamos importar el VentaDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface QuesoApi {
    // La que ya teníamos para descargar el inventario
    @GET("api/quesos")
    suspend fun obtenerProductos(): List<ProductoDto>

    // ¡ESTA ES LA LÍNEA QUE FALTA PARA QUITAR EL ERROR!
    // Revisa en tu Swagger si la ruta para guardar ventas es "api/ventas" o "api/venta"
    @POST("api/ventas")
    suspend fun registrarVentaEnNube(@Body venta: VentaDto)

    @POST("api/auth/login") // Revisa la ruta exacta en tu Swagger
    suspend fun iniciarSesion(@Body request: LoginRequest): LoginResponse
}