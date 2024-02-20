package org.hammernshield.airqualityserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AirqualityServerApplication

fun main(args: Array<String>) {
    runApplication<AirqualityServerApplication>(*args)
}
