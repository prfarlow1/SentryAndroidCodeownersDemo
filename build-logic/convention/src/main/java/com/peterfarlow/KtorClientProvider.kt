package com.peterfarlow

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class KtorClientProvider {
    companion object {

        val client by lazy {
            HttpClient(OkHttp) {
                expectSuccess = true // expectSuccess configures Ktor to throw ResponseExceptions on non-200 responses
                engine {
                    preconfigured = defaultOkHttp()
                }
                install(HttpTimeout) {
                    // set defaults to match OkHttp
                    connectTimeoutMillis = 10_000
                    socketTimeoutMillis = 10_000
                }
            }
        }

        private fun defaultOkHttp(): OkHttpClient {
            val logger = HttpLoggingInterceptor.Logger { message -> println(message) }
            return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor(logger).apply { level = HttpLoggingInterceptor.Level.BODY })
                .build()
        }
    }
}


