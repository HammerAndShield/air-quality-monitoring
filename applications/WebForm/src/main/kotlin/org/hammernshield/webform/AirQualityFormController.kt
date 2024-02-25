package org.hammernshield.webform

import org.hammernshield.AirQualityData
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.HttpServerErrorException.InternalServerError
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class AirQualityFormController(val webClient: WebClient.Builder) {

    val restUrl = System.getenv("REST_URL")

    @GetMapping("/")
    fun homeRequest(model: Model): String {
        try {
            val url = "$restUrl/air-quality/"

            val airQualityDataMono: Mono<AirQualityData> = webClient.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(AirQualityData::class.java)

            val airQualityData = airQualityDataMono.blockOptional().orElse(null)
            if (airQualityData != null) {
                model.addAttribute("airQualityData", airQualityData)
            } else {
                model.addAttribute("error", "Failed to fetch air quality data.")
            }
            return "home"
        } catch (e: Exception) {
            model.addAttribute("error", "Failed to fetch air quality data.")
            return "home"
        }
    }

    @GetMapping("/latest")
    fun latestRequest(model: Model): String {
        try {
            val url = "$restUrl/air-quality/recent"

            val airQualityDataFlux: Flux<AirQualityData> = webClient.build().get()
                .uri(url)
                .retrieve()
                .bodyToFlux(AirQualityData::class.java)

            val airQualityDataList = airQualityDataFlux.collectList().blockOptional().orElse(null)
            val averageAqi = airQualityDataList?.map { it.overallAqi }?.average()?.toInt()

            if (airQualityDataList != null) {
                model.addAttribute("airQualityDataList", airQualityDataList)
                model.addAttribute("averageAqi", averageAqi ?: 0)
            } else {
                model.addAttribute("error", "Failed to fetch air quality data.")
            }

            return "latest"
        } catch (e: Exception) {
            model.addAttribute("error", "Failed to fetch air quality data.")
            return "latest"
        }
    }


    @GetMapping("/latest/records")
    fun bulkRequest(@RequestParam amount: Int, model: Model): String {
        try {
            val url = "$restUrl/air-quality/records?x=$amount"

            val airQualityDataFlux: Flux<AirQualityData> = webClient.build().get()
                .uri(url)
                .retrieve()
                .bodyToFlux(AirQualityData::class.java)

            val airQualityDataList = airQualityDataFlux.collectList().blockOptional().orElse(null)
            val averageAqi = airQualityDataList?.map { it.overallAqi }?.average()?.toInt()

            if (airQualityDataList != null) {
                model.addAttribute("airQualityDataList", airQualityDataList)
                model.addAttribute("averageAqi", averageAqi ?: 0)
                model.addAttribute("recordCount", amount)
            } else {
                model.addAttribute("error", "Failed to fetch air quality data.")

            }
            return "latest"
        } catch (e: Exception) {
            model.addAttribute("error", "Failed to fetch air quality data.")
            return "latest"
        }
    }

}