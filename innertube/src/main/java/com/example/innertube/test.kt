package com.example.innertube

suspend fun main() {
    val innertube = Innertube()
    val channel = innertube.channel(id = "UC-I6EZOs3DqLmfD9shNT28A")
    println(channel)
    println(channel.avatar)
    println(channel.videos)
    println(channel.videos.size)
}