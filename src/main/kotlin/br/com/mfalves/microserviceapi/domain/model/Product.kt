package br.com.mfalves.microserviceapi.domain.model

import java.math.BigDecimal

data class Product(
    val name: String,
    val price: BigDecimal
)

data class ProductPersist(
    val productId: String,
    val name: String,
    val price: BigDecimal
)
