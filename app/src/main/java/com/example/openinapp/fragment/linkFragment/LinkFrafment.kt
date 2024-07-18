package com.example.openinapp.fragment.linkFragment

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.openinapp.fragment.vmLinkFragment.ListViewModel
import com.example.openinapp.R
import com.example.openinapp.databinding.FragmentLinkFrafmentBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter


class LinkFrafment : Fragment() {

    private lateinit var binding: FragmentLinkFrafmentBinding
    private lateinit var listView: ListView

    private val listViewModel: ListViewModel by viewModels()
    private lateinit var customListAdapter: CustomListAdapter
    private lateinit var customListAdapter2: CustomListAdapter2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentLinkFrafmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveToken(requireContext(),"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI")
        getToken(requireContext())?.let { listViewModel.loadDashboardData(it, requireContext()) }
        listView=binding.lvTopLinkList
        customListAdapter = CustomListAdapter(requireContext(), emptyList())
        customListAdapter2 = CustomListAdapter2(requireContext(), emptyList())
        listView.adapter = customListAdapter
        listViewFun()
        chartGraph()
        onClick()
    }

    private fun onClick() {
        binding.btnTopLink.setOnClickListener {
            listView.adapter = customListAdapter
            listViewFun()
            selectBtnChangeUI(it)

        }
        binding.btnRecentLink.setOnClickListener {
            listView.adapter = customListAdapter2
            listViewFun()
            selectBtnChangeUI(it)
        }
    }

    private fun selectBtnChangeUI(v : View) {
        val colorClicked = resources.getColor(R.color.appColor)
        val colorNotClicked = resources.getColor(R.color.transperent)
        val textColorC = resources.getColor(R.color.white)
        val textColorN = resources.getColor(R.color.gray)
        // Reset all buttons to not clicked state
        binding.btnRecentLink.apply {
            background.setColorFilter(colorNotClicked, PorterDuff.Mode.SRC_ATOP)
            setTextColor(textColorN)
        }
        binding.btnTopLink.apply {
            background.setColorFilter(colorNotClicked, PorterDuff.Mode.SRC_ATOP)
            setTextColor(textColorN)
        }

        v.background.setColorFilter(colorClicked, PorterDuff.Mode.SRC_ATOP)
        (v as Button).setTextColor(textColorC)

    }

    private fun listViewFun() {


        listViewModel.itemsTopLinks.observe(viewLifecycleOwner, Observer { items ->

            customListAdapter.updateItemsTL(items)
            setListViewHeightBasedOnItems(listView)
        })

        listViewModel.items.observe(viewLifecycleOwner, Observer { items ->

            customListAdapter2.updateItemsRL(items)
            setListViewHeightBasedOnItems(listView)
        })

    }

    private fun setListViewHeightBasedOnItems(listView: ListView) {
        val listAdapter = listView.adapter ?: return

        val params = listView.layoutParams
        params.height = 230 * (listAdapter.count)
        listView.layoutParams = params
        listView.requestLayout()
    }

    private fun chartGraph() {
        var entries: List<Entry>? = null
        listViewModel.chartData.observe(viewLifecycleOwner, Observer { chartData ->
                entries = chartData.entries.mapIndexed { index, entry ->
                Entry(index.toFloat(), entry.value.toFloat())
            }

            val dataSet = LineDataSet(entries, "Overall URL Chart").apply {
                color = ContextCompat.getColor(requireContext(), R.color.appColor)
                valueTextColor = Color.BLACK
                setDrawFilled(true)
                fillColor = ContextCompat.getColor(requireContext(), R.color.appColor)
                fillAlpha = 50
            }

            val lineData = LineData(dataSet)

            binding.chart.apply {
                data = lineData
                axisLeft.axisMinimum = 0f
                axisRight.isEnabled = false

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(chartData.keys.toList())
                }

                description.isEnabled = false
                legend.isEnabled = false
                invalidate() // Refresh the chart
            }
        })
        if (entries==null){
            // Create dummy data for the chart
            val entries = listOf(
                Entry(0f, 20f),
                Entry(1f, 30f),
                Entry(2f, 50f),
                Entry(3f, 50f),
                Entry(4f, 80f),
                Entry(5f, 90f),
                Entry(6f, 30f),
                Entry(7f, 70f)
            )

            // Create a LineDataSet with entries
            val dataSet = LineDataSet(entries, "Label").apply {

                color = ContextCompat.getColor(requireContext(), R.color.appColor)
                valueTextColor = Color.BLACK
                setDrawFilled(true)
                fillColor = ContextCompat.getColor(requireContext(), R.color.appColor)
                fillAlpha = 50
            }

            // Create LineData with the dataSet
            val lineData = LineData(dataSet)

            // Customize the LineChart
            binding.chart.apply {
                data = lineData
                axisLeft.axisMinimum = 0f
                axisRight.isEnabled = false

                // Customize X-axis
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    valueFormatter = MonthValueFormatter()
                }

                description.isEnabled = false
                legend.isEnabled = false
                invalidate() // Refresh the chart
            }
            }
    }

    fun saveToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("bearer_token", token)
            apply()
        }
    }

    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("bearer_token", null)
    }
}




class MonthValueFormatter : ValueFormatter() {
    private val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    override fun getFormattedValue(value: Float): String {
        val index = value.toInt()
        return if (index >= 0 && index < months.size) {
            months[index]
        } else {
            ""
        }
    }
}

