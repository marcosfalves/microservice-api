package br.com.mfalves.microserviceapi.domain.repository

import br.com.mfalves.microserviceapi.domain.model.ProductPersist
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse

@Repository
class ProductRepository (

    private val client: DynamoDbAsyncClient,

    @Value("\${application.dynamo.product-table-name}")
    private val productTableName: String
    ) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun saveProduct(product: ProductPersist): Mono<PutItemResponse> {
        val putItemRequest = PutItemRequest.builder()
            .item(
                mapOf(
                    "productId" to AttributeValue.builder().s(product.productId).build(),
                    "name" to AttributeValue.builder().s(product.name).build(),
                    "price" to AttributeValue.builder().s(product.price?.toString()).build()
                )
            )
            .tableName(productTableName)
            .build()

        return Mono.fromFuture(client.putItem(putItemRequest)).doOnError {
            logger.error("Exception while saving Product information - $it")
        }
    }

    fun findProduct(productId: String): Mono<ProductPersist> {
        val getItemRequest = GetItemRequest.builder()
            .key(
                mapOf(
                    "productId" to AttributeValue.builder().s(productId).build()
                )
            )
            .tableName(productTableName)
            .build()

        return Mono.fromFuture(client.getItem(getItemRequest))
            .map { response ->
                ProductPersist(
                    productId,
                    (response.item()["name"] ?: error("name N/A")).s(),
                    (response.item()["price"] ?: error("price N/A")).s().toBigDecimal())
            }
            .doOnError { logger.error("Exception while retrieving Product information - $it") }
    }

}