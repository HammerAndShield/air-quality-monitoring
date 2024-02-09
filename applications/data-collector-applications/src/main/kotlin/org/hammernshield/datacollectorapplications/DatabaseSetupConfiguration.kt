package org.hammernshield.datacollectorapplications

import jakarta.annotation.PostConstruct
import org.hammernshield.AirQualityDataService
import org.hammernshield.AirQualityDataServiceImpl
import org.hammernshield.DatabaseConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseSetupConfiguration {

    @PostConstruct
    fun initializeDatabase() {
        DatabaseConfig.init()
    }

    @Bean
    fun provideAirQualityDataService(): AirQualityDataService {
        return AirQualityDataServiceImpl()
    }

}