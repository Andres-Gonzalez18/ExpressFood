package edu.proyecto.expressfoodapp

import edu.proyecto.expressfoodapp.data.model.DailyReport
import org.junit.Assert.assertEquals
import org.junit.Test

class DailyReportTest {

    @Test
    fun dailyReport_storesCorrectValues() {

        val report = DailyReport(
            date = "12/06/2026",
            orderCount = 5,
            totalAmount = 25000.0
        )

        assertEquals("12/06/2026", report.date)
        assertEquals(5, report.orderCount)
        assertEquals(25000.0, report.totalAmount, 0.0)
    }
}