package org.hammernshield.airqualityserver

import org.hammernshield.AirQualityData
import org.hammernshield.AirQualityDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AirQualityController(@Autowired val airQualityService: AirQualityDataService) {

    @GetMapping("/air-quality/latest")
    fun getMostRecentAirQualityRecord(): ResponseEntity<AirQualityData> {
        return try {
            val mostRecentRecord = airQualityService.getMostRecentAirQualityRecord()
            ResponseEntity.ok(mostRecentRecord)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/air-quality/past-3-days")
    fun getPast3DaysAirQualityRecords(): ResponseEntity<List<AirQualityData>> {
        return try {
            val records = airQualityService.getPast3DaysAirQualityData()
            ResponseEntity.ok(records)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/air-quality/get-x-records")
    fun getXAirQualityRecords(
        @RequestParam x: Int
    ): ResponseEntity<List<AirQualityData>> {
        return try {
            val records = airQualityService.getXPastRecords(x)
            ResponseEntity.ok(records)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

}