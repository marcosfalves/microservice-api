package br.com.mfalves.microserviceapi.api.controller

import br.com.mfalves.microserviceapi.domain.model.Product
import br.com.mfalves.microserviceapi.domain.model.ProductPersist
import br.com.mfalves.microserviceapi.domain.repository.ProductRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.json
import reactor.core.publisher.Mono
import java.util.*

@Component
class ProductHandler(private val productRepo: ProductRepository) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun saveProductInformation(request: ServerRequest): Mono<ServerResponse> {
        val productId: String = generateProductId()

        return request.bodyToMono(Product::class.java)
            .map { ProductPersist(productId, it.name, it.price) }
            .flatMap { productRepo.saveProduct(it) }
            .flatMap { ServerResponse.ok().json().bodyValue("{\"productId\": \"$productId\"}") }
            .doOnError { logger.error("Exception while trying to store a new product record - $it") }
    }

    fun findProductInformation(request: ServerRequest): Mono<ServerResponse> {

        return Mono.fromSupplier { request.pathVariable("productId") }
            .flatMap { productRepo.findProduct(it) }
            .flatMap { ServerResponse.ok().json().body(Mono.just(it), ProductPersist::class.java) }
            .doOnError { logger.error("Exception while trying to retrieve a product record - $it") }
    }

    private fun generateProductId(): String {
        return UUID.randomUUID().toString()
    }
}