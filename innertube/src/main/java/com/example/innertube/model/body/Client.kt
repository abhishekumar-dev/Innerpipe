package com.example.innertube.model.body

import kotlinx.serialization.Serializable

/** For Apple device names, see [here](https://gist.github.com/adamawolf/3048717)
 * For iOS versions, see [here](https://en.wikipedia.org/wiki/IOS_version_history#Releases),
 * then go to the dedicated article of the major version you want.
 */
@Serializable
data class Client(
    val hl: String,
    val clientName: String,
    val clientVersion: String,
    val deviceModel: String = "",
    val userAgent: String
) {
    companion object {
        val IOS = Client(
            hl = "en",
            clientName = "IOS",
            clientVersion = "19.09.37",
            deviceModel = "iPhone14,3",
            userAgent = "com.google.ios.youtube/19.09.37 (iPhone14,3; U; CPU iOS 15_6 like Mac OS X)"
        )
        val WEB = Client(
            hl = "en",
            clientName = "WEB",
            clientVersion = "2.20240906.01.00",
            userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36,gzip(gfe)"
        )
        val TVHTML5 = Client(
            hl = "en",
            clientName = "TVHTML5_SIMPLY_EMBEDDED_PLAYER",
            clientVersion = "2.0",
            userAgent = "Mozilla/5.0 (DirectFB; Linux x86_64) Cobalt/4.13031-qa (unlike Gecko) Starboard/1"
        )
    }
}