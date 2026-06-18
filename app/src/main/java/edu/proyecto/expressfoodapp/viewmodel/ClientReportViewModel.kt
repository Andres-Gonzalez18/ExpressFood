package edu.proyecto.expressfoodapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.proyecto.expressfoodapp.data.model.DailyReport
import edu.proyecto.expressfoodapp.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClientReportViewModel : ViewModel() {

    private val repository = OrderRepository()

    private val _dailyReports = MutableStateFlow<List<DailyReport>>(emptyList())
    val dailyReports: StateFlow<List<DailyReport>> = _dailyReports

    private val _monthlyTotal = MutableStateFlow(0.0)
    val monthlyTotal: StateFlow<Double> = _monthlyTotal

    fun loadReports() {
        viewModelScope.launch {
            val orders = repository.getUserOrders()

            val grouped = orders.groupBy { order ->
                formatDate(order.date)
            }

            val reports = grouped.map { (date, ordersByDate) ->
                DailyReport(
                    date = date,
                    orderCount = ordersByDate.size,
                    totalAmount = ordersByDate.sumOf { it.total }
                )
            }.sortedByDescending { it.date }

            _dailyReports.value = reports
            _monthlyTotal.value = orders.sumOf { it.total }
        }
    }

    private fun formatDate(timestamp: Long): String {
        val formatter = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        )
        return formatter.format(Date(timestamp))
    }
}