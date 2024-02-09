package org.hammernshield.datacollectorapplications

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class DataCollectorApplicationsApplication

fun main(args: Array<String>) {
    runApplication<DataCollectorApplicationsApplication>(*args)
}
