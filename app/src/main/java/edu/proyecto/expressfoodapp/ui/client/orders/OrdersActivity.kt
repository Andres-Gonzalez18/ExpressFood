package edu.proyecto.expressfoodapp.ui.client.orders

import android.content.Intent
import android.os.Bundle
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
import edu.proyecto.expressfoodapp.databinding.ActivityOrdersBinding
import edu.proyecto.expressfoodapp.ui.auth.LoginActivity
import edu.proyecto.expressfoodapp.ui.client.ClientActivity
import edu.proyecto.expressfoodapp.ui.client.cart.CartActivity
import edu.proyecto.expressfoodapp.ui.client.reports.ClientReportActivity
import edu.proyecto.expressfoodapp.viewmodel.OrdersViewModel
import kotlinx.coroutines.launch

class OrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersBinding
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var auth: FirebaseAuth

    private val ordersViewModel: OrdersViewModel by viewModels()

    private val statusOptions = listOf(
        "TODAS",
        "PENDIENTE",
        "EN CAMINO",
        "ENTREGADA",
        "CANCELADA"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupRecyclerView()
        setupStatusFilter()
        observeOrders()
        setupNavbar()
        setupLogout()

        ordersViewModel.loadOrders()
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter()

        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(this@OrdersActivity)
            adapter = orderAdapter
        }
    }

    private fun setupStatusFilter() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statusOptions
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerStatus.adapter = adapter

        binding.spinnerStatus.setOnItemSelectedListener(
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedStatus = statusOptions[position]
                    ordersViewModel.applyFilter(selectedStatus)
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }
        )
    }

    private fun observeOrders() {
        lifecycleScope.launch {
            ordersViewModel.filteredOrders.collect { orders ->
                orderAdapter.submitList(orders)
            }
        }
    }

    private fun setupNavbar() {
        binding.btnOpenOrders.setBackgroundColor(getColor(R.color.express_primary_light))
        binding.btnOpenOrders.setTextColor(getColor(R.color.express_primary_dark))

        binding.btnOpenMenu.setOnClickListener {
            startActivity(Intent(this, ClientActivity::class.java))
            finish()
        }
        binding.btnOpenCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
            finish()
        }
        binding.btnClientReports.setOnClickListener {
            startActivity(Intent(this, ClientReportActivity::class.java))
            finish()
        }
    }

    private fun setupLogout() {
        binding.header.btnLogout.visibility = View.GONE
    }
}