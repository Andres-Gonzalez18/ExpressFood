package edu.proyecto.expressfoodapp.ui.admin.orders

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import edu.proyecto.expressfoodapp.R
import edu.proyecto.expressfoodapp.data.model.Order
import edu.proyecto.expressfoodapp.databinding.ActivityAdminOrdersBinding
import edu.proyecto.expressfoodapp.ui.admin.AdminActivity
import edu.proyecto.expressfoodapp.ui.admin.reports.AdminReportActivity
import edu.proyecto.expressfoodapp.ui.auth.LoginActivity
import edu.proyecto.expressfoodapp.viewmodel.AdminOrdersViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminOrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminOrdersBinding
    private lateinit var adminOrderAdapter: AdminOrderAdapter
    private lateinit var auth: FirebaseAuth

    private val adminOrdersViewModel: AdminOrdersViewModel by viewModels()
    private var allOrders: List<Order> = emptyList()

    private val statusOptions = listOf("TODAS", "PENDIENTE", "EN CAMINO", "ENTREGADA", "CANCELADA")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupRecyclerView()
        setupStatusFilter()
        setupTextFilters()
        observeOrders()
        setupNavbar()
        setupLogout()

        adminOrdersViewModel.loadOrders()
    }

    private fun setupRecyclerView() {
        adminOrderAdapter = AdminOrderAdapter { order, newStatus ->
            adminOrdersViewModel.updateStatus(order.id, newStatus)
        }
        binding.rvAdminOrders.apply {
            layoutManager = LinearLayoutManager(this@AdminOrdersActivity)
            adapter = adminOrderAdapter
        }
    }

    private fun setupNavbar() {
        // Indicador visual: Estamos en Órdenes
        binding.btnOpenAdminOrders.setBackgroundColor(getColor(R.color.express_primary_light))
        binding.btnOpenAdminOrders.setTextColor(getColor(R.color.express_primary_dark))

        binding.btnOpenAdminHome.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
            finish()
        }
        binding.btnAdminReports.setOnClickListener {
            startActivity(Intent(this, AdminReportActivity::class.java))
            finish()
        }
    }

    private fun setupLogout() {
        binding.header.btnLogout.visibility = View.GONE // Oculto en secundarias si prefieres, o VISIBLE si quieres salir desde aquí
    }

    private fun setupStatusFilter() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatusFilter.adapter = adapter
        binding.spinnerStatusFilter.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: android.widget.AdapterView<*>?, p1: View?, position: Int, p3: Long) { applyFilters() }
            override fun onNothingSelected(p0: android.widget.AdapterView<*>?) {}
        }
    }

    private fun setupTextFilters() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { applyFilters() }
            override fun afterTextChanged(p0: Editable?) {}
        }
        binding.etClientFilter.addTextChangedListener(watcher)
        binding.etDateFilter.addTextChangedListener(watcher)
    }

    private fun observeOrders() {
        lifecycleScope.launch {
            adminOrdersViewModel.orders.collect { orders ->
                allOrders = orders
                applyFilters()
            }
        }
    }

    private fun applyFilters() {
        val selectedStatus = binding.spinnerStatusFilter.selectedItem?.toString() ?: "TODAS"
        val clientQuery = binding.etClientFilter.text.toString().trim().lowercase()
        val dateQuery = binding.etDateFilter.text.toString().trim()

        val filtered = allOrders.filter { order ->
            val matchesStatus = selectedStatus == "TODAS" || order.status == selectedStatus
            val matchesClient = clientQuery.isBlank() || order.userEmail.lowercase().contains(clientQuery)
            val matchesDate = dateQuery.isBlank() || formatDateOnly(order.date).contains(dateQuery)
            matchesStatus && matchesClient && matchesDate
        }
        adminOrderAdapter.submitList(filtered)
    }

    private fun formatDateOnly(timestamp: Long): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))
    }
}