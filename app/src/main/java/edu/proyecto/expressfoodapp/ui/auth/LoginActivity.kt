package edu.proyecto.expressfoodapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import edu.proyecto.expressfoodapp.R
import edu.proyecto.expressfoodapp.databinding.ActivityLoginBinding
import edu.proyecto.expressfoodapp.ui.admin.AdminActivity
import edu.proyecto.expressfoodapp.ui.client.ClientActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                auth.signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            checkUserRole()
                        } else {
                            Toast.makeText(this, "Error al autenticar con Firebase", Toast.LENGTH_LONG).show()
                        }
                    }

            } catch (e: ApiException) {
                Toast.makeText(this, "Error Google Sign-In: ${e.statusCode}", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        if (auth.currentUser != null) {
            checkUserRole()
        }

        binding.btnGoogleSignIn.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this, googleSignInOptions)
        googleSignInLauncher.launch(googleClient.signInIntent)
    }

    private fun checkUserRole() {
        val user = auth.currentUser ?: return

        firestore.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val role = document.getString("role") ?: "client"

                    if (role == "admin") {
                        goToAdmin()
                    } else {
                        goToClient()
                    }
                } else {
                    createDefaultClientUser()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al validar rol", Toast.LENGTH_LONG).show()
            }
    }

    private fun createDefaultClientUser() {
        val user = auth.currentUser ?: return

        val userData = hashMapOf(
            "uid" to user.uid,
            "email" to (user.email ?: ""),
            "displayName" to (user.displayName ?: ""),
            "role" to "client"
        )

        firestore.collection("users")
            .document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                goToClient()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al crear usuario", Toast.LENGTH_LONG).show()
            }
    }

    private fun goToClient() {
        startActivity(Intent(this, ClientActivity::class.java))
        finish()
    }

    private fun goToAdmin() {
        startActivity(Intent(this, AdminActivity::class.java))
        finish()
    }
}