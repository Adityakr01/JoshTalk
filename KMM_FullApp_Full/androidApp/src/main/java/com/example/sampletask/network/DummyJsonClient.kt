package com.example.sampletask.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.engine.okhttp.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Serializable
data class Product(val id:Int, val title:String, val description:String)

class DummyJsonClient {
    private val client = HttpClient(OkHttp)

    suspend fun fetchProduct(productId:Int = 1): Product? = withContext(Dispatchers.IO) {
        try {
            val url = "https://dummyjson.com/products/$productId"
            val txt: String = client.get(url).body()
            // crude parse using kotlinx.serialization Json
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString(Product.serializer(), txt)
        } catch (e: Exception) {
            null
        }
    }
}
