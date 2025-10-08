// F_ARQUIVO: data/CepApiService.kt
package com.tazy.simplepillfinal.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CepApiService {
    @GET("{cep}/json/")
    suspend fun getAddress(@Path("cep") cep: String): CepResponse

    companion object {
        fun create(): CepApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(CepApiService::class.java)
        }
    }
}