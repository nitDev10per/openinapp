package com.example.openinapp.dataClass

data class Data(
    val recent_links: List<RecentLink>,
    val top_links: List<TopLink>,
    val overall_url_chart: Map<String, Int>
)