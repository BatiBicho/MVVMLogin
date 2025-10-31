package com.pompurin.mvvmlogin.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pompurin.mvvmlogin.data.repository.Resource
import com.pompurin.mvvmlogin.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String> = _confirmPassword

    private val _birthDate = MutableLiveData<String>()
    val birthDate: LiveData<String> = _birthDate

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    private val _registerEnable = MutableLiveData<Boolean>()
    val registerEnable: LiveData<Boolean> = _registerEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData para mostrar mensajes de error o éxito
    private val _registerResult = MutableLiveData<String>()
    val registerResult: LiveData<String> = _registerResult

    fun onRegisterChanged(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        birthDate: String,
        address: String
    ) {
        _name.value = name
        _email.value = email
        _password.value = password
        _confirmPassword.value = confirmPassword
        _birthDate.value = birthDate
        _address.value = address

        _registerEnable.value = isValidName(name) &&
                isValidEmail(email) &&
                isValidPassword(password) &&
                passwordsMatch(password, confirmPassword) &&
                isValidBirthDate(birthDate) &&
                isValidAddress(address)
    }

    private fun isValidName(name: String): Boolean {
        return name.length >= 2
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun passwordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword && password.isNotEmpty()
    }

    private fun isValidBirthDate(birthDate: String): Boolean {
        return birthDate.isNotEmpty() && birthDate.length >= 8
    }

    private fun isValidAddress(address: String): Boolean {
        return address.length >= 5
    }

    fun onRegisterSelected() {
        viewModelScope.launch {
            _isLoading.value = true

            // Llamada real a la API
            val result = userRepository.register(
                name = _name.value ?: "",
                email = _email.value ?: "",
                password = _password.value ?: "",
                birthDate = _birthDate.value ?: "",
                address = _address.value ?: ""
            )

            _isLoading.value = false

            when (result) {
                is Resource.Success -> {
                    _registerResult.value = "Registro exitoso: ${result.data.name}"
                    // Aquí puedes guardar el usuario en SharedPreferences o Room
                }
                is Resource.Error -> {
                    _registerResult.value = "Error: ${result.message}"
                }
                is Resource.Loading -> {
                    // Ya manejado con _isLoading
                }
            }
        }
    }
}