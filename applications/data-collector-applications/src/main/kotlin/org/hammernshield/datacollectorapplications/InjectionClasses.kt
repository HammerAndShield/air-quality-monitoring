package org.hammernshield.datacollectorapplications

import org.hammernshield.datacollector.AirQualityCollector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InjectionClasses {

    @Bean
    fun provideDataCollectorClient(): AirQualityCollector {
        return AirQualityCollector("DudkR2rZIOz9EAmHGqcR4w==eZCXO2M1e9769tgq")
    }

}