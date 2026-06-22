package edu.proyecto.expressfoodapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.proyecto.expressfoodapp.data.model.DailyReport
import edu.proyecto.expressfoodapp.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AdminReportViewModel : ViewModel() {

    private val repository = OrderRepository()

    private val _dailyReports = MutableStateFlow<List<DailyReport>>(emptyList())
    val dailyReports: StateFlow<List<DailyReport>> = _dailyReports

    private val _monthlyTotal = MutableStateFlow(0.0)
    val monthlyTotal: StateFlow<Double> = _monthlyTotal

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadReports() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val orders = repository.getAllOrders()

                val groupedByDay = orders.groupBy { order ->
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = order.date
                    cal.set(Calendar.HOUR_OF_DAY, 0)
                    cal.set(Calendar.MINUTE, 0)
                    cal.set(Calendar.SECOND, 0)
                    cal.set(Calendar.MILLISECOND, 0)
                    cal.timeInMillis
                }

                val reportsSorted = groupedByDay.keys.sortedDescending().map { timestamp ->
                    val ordersByDate = groupedByDay[timestamp] ?: emptyList()
                    DailyReport(
                        date = formatDate(timestamp),
                        orderCount = ordersByDate.size,
                        totalAmount = ordersByDate.sumOf { it.total }
                    )
                }

                _dailyReports.value = reportsSorted
                _monthlyTotal.value = orders.sumOf { it.total }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun formatDate(timestamp: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }
}