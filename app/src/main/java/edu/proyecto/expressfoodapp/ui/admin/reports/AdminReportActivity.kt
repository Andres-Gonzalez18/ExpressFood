package edu.proyecto.expressfoodapp.ui.admin.reports

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import edu.proyecto.expressfoodapp.R
import edu.proyecto.expressfoodapp.databinding.ActivityAdminReportBinding
import edu.proyecto.expressfoodapp.ui.admin.AdminActivity
import edu.proyecto.expressfoodapp.ui.admin.orders.AdminOrdersActivity
import edu.proyecto.expressfoodapp.ui.client.reports.ReportAdapter
import edu.proyecto.expressfoodapp.viewmodel.AdminReportViewModel
import kotlinx.coroutines.launch

class AdminReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminReportBinding
    private lateinit var reportAdapter: ReportAdapter
    private lateinit var auth: FirebaseAuth

    private val adminReportViewModel: AdminReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupRecyclerView()
        observeReports()
        setupNavbar()
        setupLogout()

        adminReportViewModel.loadReports()
    }

    private fun setupRecyclerView() {
        reportAdapter = ReportAdapter()
        binding.rvAdminReports.apply {
            layoutManager = LinearLayoutManager(this@AdminReportActivity)
            adapter = reportAdapter
        }
    }

    private fun observeReports() {
        lifecycleScope.launch {
            adminReportViewModel.dailyReports.collect { reports ->
                reportAdapter.submitList(reports)
            }
        }
        lifecycleScope.launch {
            adminReportViewModel.monthlyTotal.collect { total ->
                binding.tvAdminMonthlyTotal.text = "Total: ₡$total"
            }
        }
    }

    private fun setupNavbar() {
        // Forzamos el color de selección para que no se "baje" visualmente
        binding.btnAdminReports.setBackgroundColor(getColor(R.color.express_primary_light))
        binding.btnAdminReports.setTextColor(getColor(R.color.express_primary_dark))

        binding.btnOpenAdminHome.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
            finish()
        }
        binding.btnOpenAdminOrders.setOnClickListener {
            startActivity(Intent(this, AdminOrdersActivity::class.java))
            finish()
        }
    }

    private fun setupLogout() {
        binding.header.btnLogout.visibility = View.GONE
    }
}