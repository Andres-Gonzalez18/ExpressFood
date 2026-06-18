package edu.proyecto.expressfoodapp.ui.client.reports

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import edu.proyecto.expressfoodapp.R
import edu.proyecto.expressfoodapp.databinding.ActivityClientReportBinding
import edu.proyecto.expressfoodapp.ui.auth.LoginActivity
import edu.proyecto.expressfoodapp.ui.client.ClientActivity
import edu.proyecto.expressfoodapp.ui.client.cart.CartActivity
import edu.proyecto.expressfoodapp.ui.client.orders.OrdersActivity
import edu.proyecto.expressfoodapp.viewmodel.ClientReportViewModel
import kotlinx.coroutines.launch

class ClientReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientReportBinding
    private lateinit var reportAdapter: ReportAdapter
    private lateinit var auth: FirebaseAuth

    private val clientReportViewModel: ClientReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupRecyclerView()
        observeReports()
        setupNavbar()
        setupLogout()

        clientReportViewModel.loadReports()
    }

    private fun setupRecyclerView() {
        reportAdapter = ReportAdapter()
        binding.rvClientReports.apply {
            layoutManager = LinearLayoutManager(this@ClientReportActivity)
            adapter = reportAdapter
        }
    }

    private fun observeReports() {
        lifecycleScope.launch {
            clientReportViewModel.dailyReports.collect { reports ->
                reportAdapter.submitList(reports)
            }
        }
        lifecycleScope.launch {
            clientReportViewModel.monthlyTotal.collect { total ->
                binding.tvClientMonthlyTotal.text = "Total mensual: ₡$total"
            }
        }
    }

    private fun setupNavbar() {
        // Indicador visual: Estamos en Reportes
        binding.btnClientReports.setBackgroundColor(getColor(R.color.express_primary_light))
        binding.btnClientReports.setTextColor(getColor(R.color.express_primary_dark))

        binding.btnOpenMenu.setOnClickListener {
            startActivity(Intent(this, ClientActivity::class.java))
            finish()
        }
        binding.btnOpenOrders.setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
            finish()
        }
        binding.btnOpenCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
            finish()
        }
    }

    private fun setupLogout() {
        binding.header.btnLogout.visibility = View.GONE
    }
}