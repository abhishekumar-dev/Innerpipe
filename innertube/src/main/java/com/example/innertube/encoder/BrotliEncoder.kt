package com.example.innertube.encoder

import io.ktor.client.plugins.compression.*
import io.ktor.util.ContentEncoder
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import org.brotli.dec.BrotliInputStream
import kotlin.coroutines.CoroutineContext

object BrotliEncoder : ContentEncoder {
    override val name: String = "br"

    override fun decode(source: ByteReadChannel, coroutineContext: CoroutineContext): ByteReadChannel =
        BrotliInputStream(source.toInputStream()).toByteReadChannel()

    override fun encode(source: ByteReadChannel, coroutineContext: CoroutineContext): ByteReadChannel =
        throw UnsupportedOperationException("Encode not implemented by the library yet.")

    override fun encode(source: ByteWriteChannel, coroutineContext: CoroutineContext): ByteWriteChannel =
        throw UnsupportedOperationException("Encode not implemented by the library yet.")
}

fun ContentEncodingConfig.brotli(quality: Float? = null) {
    customEncoder(BrotliEncoder, quality)
}