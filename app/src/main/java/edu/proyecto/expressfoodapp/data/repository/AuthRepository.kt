package edu.proyecto.expressfoodapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun getCurrentUser() = auth.currentUser

    fun getUserRole(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val user = auth.currentUser

        if (user == null) {
            onError("Usuario no autenticado")
            return
        }

        firestore.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val role = document.getString("role") ?: "client"
                    onSuccess(role)
                } else {
                    createDefaultClientUser(
                        onSuccess = { onSuccess("client") },
                        onError = onError
                    )
                }
            }
            .addOnFailureListener {
                onError("Error al obtener rol del usuario")
            }
    }

    private fun createDefaultClientUser(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = auth.currentUser

        if (user == null) {
            onError("Usuario no autenticado")
            return
        }

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
                onSuccess()
            }
            .addOnFailureListener {
                onError("Error al crear usuario")
            }
    }

    fun logout() {
        auth.signOut()
    }
}