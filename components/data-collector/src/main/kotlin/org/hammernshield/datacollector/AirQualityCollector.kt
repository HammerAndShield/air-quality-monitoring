package org.hammernshield.datacollector

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*


class AirQualityCollector(
    private val apiKey: String,
    private val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }
    }
) {

    suspend fun fetchAirQuality(lat: Double, lon: Double): ApiNinjasAirQualityResponse {
        val url = "https://api.api-ninjas.com/v1/airquality?lat=$lat&lon=$lon"
        val response: HttpResponse = client.get(url) {
            header("X-Api-Key", apiKey)
            accept(ContentType.Application.Json)
        }

        return response.body()
    }

}