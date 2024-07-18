package com.example.openinapp.fragment.vmLinkFragment

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openinapp.apiCall.RetrofitClient
import com.example.openinapp.dataClass.RecentLink
import com.example.openinapp.dataClass.TopLink
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {

    private val repository = DashboardRepository(RetrofitClient.apiService)

    private val _itemsTopLinks = MutableLiveData<List<TopLink>>()
    val itemsTopLinks: LiveData<List<TopLink>> get() = _itemsTopLinks

    private val _items = MutableLiveData<List<RecentLink>>()
    val items: LiveData<List<RecentLink>> get() = _items

    private val _chartData = MutableLiveData<Map<String, Int>>()
    val chartData: LiveData<Map<String, Int>> get() = _chartData

    fun loadDashboardData(apiKey: String, context: Context) {
        viewModelScope.launch {
            try {
                val response = repository.fetchDashboardData(apiKey)
                _items.value = response.data.recent_links
                _itemsTopLinks.value = response.data.top_links

                if (response.data.overall_url_chart!=null){
                    _chartData.value = response.data.overall_url_chart
                }

                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                // Handle exceptions
                Toast.makeText(context, "${e}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
