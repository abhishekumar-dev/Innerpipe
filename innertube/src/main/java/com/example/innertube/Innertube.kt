package com.example.innertube

import com.example.innertube.encoder.brotli
import com.example.innertube.model.Player
import com.example.innertube.model.Search
import com.example.innertube.model.body.Client
import com.example.innertube.model.body.Context
import com.example.innertube.model.body.PlayerBody
import com.example.innertube.model.body.SearchBody
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
class Innertube {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            val json = Json {
                ignoreUnknownKeys = true
                explicitNulls = false
                encodeDefaults = true
            }
            json(json)
        }
        install(ContentEncoding) {
            brotli(1.0F)
            gzip(0.9F)
            deflate(0.8F)
        }
        expectSuccess = true
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "www.youtube.com"
                pathSegments = listOf("youtubei", "v1", "")
                parameters.append("prettyPrint", "false")
            }
            contentType(ContentType.Application.Json)
        }
    }

    private suspend inline fun <reified T> yt(path: String, body: T) = httpClient.post {
        url {
            appendPathSegments(path)
        }
        setBody(body)
    }

    suspend fun player(id: String): Player {
        return yt(
            path = "player",
            body = PlayerBody(context = Context(Client.IOS), videoId = id)
        ).body()
    }

    suspend fun search(query: String? = null, token: String? = null): Search {
        return yt(
            path = "search",
            body = SearchBody(context = Context(Client.WEB), query = query, continuation = token)
        ).body()
    }

    suspend fun suggestions(query: String): List<String> {
        return httpClient.get("https://suggestqueries-clients6.youtube.com/complete/search") {
            url {
                parameters.append("client", "youtube")
                parameters.append("ds", "yt")
                parameters.append("q", query)
            }
        }.bodyAsText().toListOfSuggestions()
    }
}