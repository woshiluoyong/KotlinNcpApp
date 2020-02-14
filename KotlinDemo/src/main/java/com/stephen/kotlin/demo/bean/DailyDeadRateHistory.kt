package com.stephen.kotlin.demo.bean

data class DailyDeadRateHistory(
    val countryConfirm: Int,
    val countryDead: Int,
    val countryRate: String,
    val date: String,
    val hubeiConfirm: Int,
    val hubeiDead: Int,
    val hubeiRate: String,
    val notHubeiRate: String
)