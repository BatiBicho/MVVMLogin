package com.pompurin.mvvmlogin.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class RegisterViewModel : ViewModel() {

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
        // Validación básica: verificar que no esté vacío y tenga longitud mínima
        // Puedes hacer una validación más compleja si lo necesitas (formato DD/MM/YYYY)
        return birthDate.isNotEmpty() && birthDate.length >= 8
    }

    private fun isValidAddress(address: String): Boolean {
        // Validación básica: verificar que tenga al menos 5 caracteres
        return address.length >= 5
    }

    suspend fun onRegisterSelected() {
        _isLoading.value = true
        // Simular llamada a API o proceso de registro
        delay(2000)
        // Aquí iría la lógica real de registro
        _isLoading.value = false
    }
}