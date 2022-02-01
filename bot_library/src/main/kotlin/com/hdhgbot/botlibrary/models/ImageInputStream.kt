package com.hdhgbot.botlibrary.models

import java.io.InputStream

data class ImageInputStream(
    val inputStream: InputStream,
    val fileName: String
)