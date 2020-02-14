package com.stephen.kotlin.demo.bean

data class MainData(
    val areaTree: List<AreaTree>,
    val articleList: List<Article>,
    val chinaAdd: ChinaAdd,
    val chinaDayAddList: List<ChinaDayAdd>,
    val chinaDayList: List<ChinaDay>,
    val chinaTotal: ChinaTotal,
    val dailyDeadRateHistory: List<DailyDeadRateHistory>,
    val dailyNewAddHistory: List<DailyNewAddHistory>,
    val confirmAddRank: List<ConfirmAddRank>,
    val isShowAdd: Boolean,
    val lastUpdateTime: String
)