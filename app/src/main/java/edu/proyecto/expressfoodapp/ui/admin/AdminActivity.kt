package edu.proyecto.expressfoodapp.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import edu.proyecto.expressfoodapp.R
import edu.proyecto.expressfoodapp.databinding.ActivityAdminBinding
import edu.proyecto.expressfoodapp.ui.admin.orders.AdminOrdersActivity
import edu.proyecto.expressfoodapp.ui.admin.reports.AdminReportActivity
import edu.proyecto.expressfoodapp.ui.auth.LoginActivity

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupNavbar()
        setupLogout()
    }

    private fun setupNavbar() {
        // Indicador visual: Estamos en Inicio
        binding.btnOpenAdminHome.setBackgroundColor(getColor(R.color.express_primary_light))
        binding.btnOpenAdminHome.setTextColor(getColor(R.color.express_primary_dark))

        binding.btnOpenAdminOrders.setOnClickListener {
            startActivity(Intent(this, AdminOrdersActivity::class.java))
        }

        binding.btnAdminReports.setOnClickListener {
            startActivity(Intent(this, AdminReportActivity::class.java))
        }
    }

    private fun setupLogout() {
        binding.header.btnLogout.visibility = View.VISIBLE
        binding.header.btnLogout.setOnClickListener {
            auth.signOut()
            val googleClient = GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build())
            googleClient.signOut().addOnCompleteListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}