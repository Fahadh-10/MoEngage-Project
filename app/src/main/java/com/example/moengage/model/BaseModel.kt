package com.example.moengage.model

data class BaseModel(
    val statusCode: Int? = null,
    val status: String? = null,
    val articles: ArrayList<Articles>? = ArrayList()
)

data class Articles(
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val urlToImage: String? = null,
    val content: String? = null,
    val url: String? = null,
    val publishedAt: String? = null,
)
