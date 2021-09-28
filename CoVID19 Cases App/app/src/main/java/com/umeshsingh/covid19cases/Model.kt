package com.umeshsingh.covid19cases

data class Model(
    val cityName: String,
    val activeCase: Int,
    val totalCase: Int,
    val deathCase: Int,
    val recCase: Int
)