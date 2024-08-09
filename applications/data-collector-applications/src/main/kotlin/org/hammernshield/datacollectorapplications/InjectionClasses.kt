package org.hammernshield.datacollectorapplications

import org.hammernshield.datacollector.AirQualityCollector
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InjectionClasses {

    @Value("\${air.quality.api.key}")
    private lateinit var apiKey: String

    @Bean
    fun provideDataCollectorClient(): AirQualityCollector {
        return AirQualityCollector(apiKey)
    }
}