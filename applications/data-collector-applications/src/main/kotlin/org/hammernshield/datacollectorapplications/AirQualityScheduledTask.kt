package org.hammernshield.datacollectorapplications

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.hammernshield.AirQualityDataService
import org.hammernshield.datacollector.AirQualityCollector
import org.slf4j.LoggerFactory
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class AirQualityScheduledTask(
    val airQualityCollector: AirQualityCollector,
    val dataService: AirQualityDataService
) {

    private val logger = LoggerFactory.getLogger(::AirQualityScheduledTask.javaClass)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Scheduled(cron = "0 0 */6 * * *")
    fun fetchAndStoreAirQualityScheduled() {
        fetchAndStoreAirQuality()
    }

    @EventListener(ContextRefreshedEvent::class)
    fun onApplicationEvent(event: ContextRefreshedEvent) {
        fetchAndStoreAirQuality()
    }

    private fun fetchAndStoreAirQuality() {
        scope.launch {
            val lat = 36.198870
            val lon = -115.262260
            try {
                val response = airQualityCollector.fetchAirQuality(lat, lon)
                val responseModel = convertToAirQualityData(response)
                dataService.insertAirQualityData(responseModel)
                logger.info("$responseModel added to database")
            } catch (e: Exception) {
                logger.error("Error collecting data: ${e.message}")
            }
        }
    }

}