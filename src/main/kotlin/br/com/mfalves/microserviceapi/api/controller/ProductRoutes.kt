package br.com.mfalves.microserviceapi.api.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class ProductRoutes(private val productHandler: ProductHandler) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun route(): RouterFunction<ServerResponse> = router {
        ("/products").nest {
            accept(MediaType.APPLICATION_JSON).nest {
                POST("", productHandler::saveProductInformation)
            }

            GET("/{productId}", productHandler::findProductInformation)
        }
    }
}