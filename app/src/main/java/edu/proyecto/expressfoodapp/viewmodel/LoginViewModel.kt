package edu.proyecto.expressfoodapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.proyecto.expressfoodapp.data.repository.AuthRepository

class LoginViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _role = MutableLiveData<String>()
    val role: LiveData<String> = _role

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun checkUserRole() {
        authRepository.getUserRole(
            onSuccess = { userRole ->
                _role.value = userRole
            },
            onError = { message ->
                _error.value = message
            }
        )
    }

    fun hasActiveSession(): Boolean {
        return authRepository.getCurrentUser() != null
    }
}