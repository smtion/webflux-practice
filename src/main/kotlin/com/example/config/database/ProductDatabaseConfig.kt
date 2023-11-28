package com.example.config.database

import com.example.config.kms.KmsAccount
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.ReactiveTransactionManager

@Configuration
@EnableR2dbcRepositories(
    basePackages = ["com.example.repository.database.product"],
    entityOperationsRef = "productR2dbcEntityOperations",
)
class ProductDatabaseConfig(
    private val kmsAccount: KmsAccount,
) : GlobalR2dbcConfig() {

    @Bean
    fun productR2dbcEntityOperations(
        @Qualifier("productDatabaseClient") databaseClient: DatabaseClient,
        converter: MappingR2dbcConverter
    ): R2dbcEntityOperations =
        R2dbcEntityTemplate(
            databaseClient,
            DefaultReactiveDataAccessStrategy(getDialect(databaseClient.connectionFactory), converter)
        )

    @Bean
    fun productDatabaseClient(
        @Qualifier("productConnectionFactory") connectionFactory: ConnectionFactory
    ): DatabaseClient = DatabaseClient.create(connectionFactory)

    @Bean("productConnectionFactory")
    override fun connectionFactory(): ConnectionFactory =
        ConnectionFactoryBuilder
            .withUrl(productConnectionProperties().url)
            .username(kmsAccount.user)
            .password(kmsAccount.password)
            .build()

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.product")
    fun productConnectionProperties() = R2dbcProperties()
}
