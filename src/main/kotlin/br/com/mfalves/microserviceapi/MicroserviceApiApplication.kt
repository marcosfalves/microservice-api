package br.com.mfalves.microserviceapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MicroserviceApiApplication

fun main(args: Array<String>) {
	runApplication<MicroserviceApiApplication>(*args)
}
