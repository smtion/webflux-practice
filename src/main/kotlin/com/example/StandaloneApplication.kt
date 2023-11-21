package com.example

import org.springframework.http.server.reactive.HttpHandler
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import reactor.netty.DisposableChannel
import reactor.netty.http.server.HttpServer

fun main() {
    val httpHandler: HttpHandler = RouterFunctions.toHttpHandler(com.example.routes())
    val reactorHttpHandler = ReactorHttpHandlerAdapter(httpHandler)
    HttpServer.create()
        .port(6060)
        .handle(reactorHttpHandler)
        .bind()
        .flatMap(DisposableChannel::onDispose)
        .block()
}

private fun routes(): RouterFunction<ServerResponse> =
    router {
        GET("") { ok().body(Mono.just("Standalone")) }
    }
