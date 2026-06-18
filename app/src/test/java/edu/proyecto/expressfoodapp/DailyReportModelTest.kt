package edu.proyecto.expressfoodapp

import edu.proyecto.expressfoodapp.data.model.DailyReport
import org.junit.Assert.assertEquals
import org.junit.Test

class DailyReportModelTest {

    @Test
    fun dailyReport_containsExpectedData() {

        val report = DailyReport(
            date = "12/06/2026",
            orderCount = 10,
            totalAmount = 50000.0
        )

        assertEquals("12/06/2026", report.date)
        assertEquals(10, report.orderCount)
        assertEquals(50000.0, report.totalAmount, 0.0)
    }
}