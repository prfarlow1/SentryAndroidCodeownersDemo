package com.peterfarlow.core.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.TypeInfo
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class HttpClient(
    private val apiKey: String
) {
    private val httpClient = HttpClient(OkHttp) {
        engine {
            config {
                addInterceptor(HttpLoggingInterceptor {
                    Timber.tag("Http").d(it)
                }.apply {
                    level = HttpLoggingInterceptor.Level.BODY
                    redactHeader("x-api-key")
                })
            }
        }
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun <T> get(url: String, typeInfo: TypeInfo): T? {
        val response = try {
            httpClient.get(url) {
                header("x-api-key", apiKey)
            }
        } catch (exception: ResponseException) {
            Timber.e(exception, "Failed to get cats")
            null
        }
        return response?.body(typeInfo)
    }
}


