package org.hammernshield.webform

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebFormApplication

fun main(args: Array<String>) {
    runApplication<WebFormApplication>(*args)
}
