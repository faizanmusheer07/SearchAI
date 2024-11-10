package com.searchai.common.models.basicModels


data class Category(
    val message: String,
    val data: Map<String, List<Subcategory>>,
    val status: Boolean
)
