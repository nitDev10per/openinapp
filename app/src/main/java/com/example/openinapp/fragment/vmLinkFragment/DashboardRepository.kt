package com.example.openinapp.fragment.vmLinkFragment

import com.example.openinapp.apiCall.ApiService
import com.example.openinapp.dataClass.ApiResponse

class DashboardRepository(private val apiService: ApiService) {

    suspend fun fetchDashboardData(apiKey: String): ApiResponse {
        return apiService.getDashboardData(apiKey)
    }
}