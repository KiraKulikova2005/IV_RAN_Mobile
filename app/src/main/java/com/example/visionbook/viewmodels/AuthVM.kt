package com.example.visionbook.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visionbook.models.api.AuthApi
import com.example.visionbook.models.dataclasses.RegistrationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthVM : ViewModel() {
    private val _fioState = MutableLiveData<String>()
    val fioState: LiveData<String> = _fioState
    private val _depState = MutableLiveData<String>()
    val depState: LiveData<String> = _depState
    private val _emailState = MutableLiveData<String>()
    val emailState: LiveData<String> = _emailState
    private val _passwordState = MutableLiveData<String>()
    val passwordState: LiveData<String> = _passwordState
    private val _secondPasswordState = MutableLiveData<String>()
    val secondPasswordState: LiveData<String> = _secondPasswordState

    private val _isAuthenticated = MutableStateFlow<Boolean?>(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated.asStateFlow()


    private val _tokenState = MutableLiveData("")
    val tokenState: LiveData<String> = _tokenState

    fun loginSuccess(token: String) {
        _tokenState.value = token
        _isAuthenticated.value = true
    }

    fun logout() {
        // Очищаем все данные аутентификации
        _tokenState.value = ""
        _isAuthenticated.value = false
        _emailState.value = ""
        _passwordState.value = ""
        _fioState.value = ""
        _depState.value = ""
        _secondPasswordState.value = ""
    }

    fun setAuthenticated(authenticated: Boolean) {
        _isAuthenticated.value = authenticated
    }

    fun checkPasswordMatch(password: String, secondPassword: String?): Boolean {
        return if (secondPassword != null) {
            password == secondPassword
        } else {
            true
        }
    }
    private fun setToken(token: String) {
        _tokenState.value = token
    }

    suspend fun registration(fio: String, dep: String, email: String, password: String, authApi: AuthApi) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authApi.userRegistration(RegistrationModel(fio, dep, email, password))
            } catch (e: Exception) {
                Log.e("AuthVM", "Registration error", e)
            }
        }
    }

    suspend fun authorization(fio: String, dep: String, email: String, password: String, authApi: AuthApi) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userToken = authApi.userLogin(RegistrationModel(fio, dep, email, password))
                Log.d("AuthVM", "Token received: ${userToken.token}")
                setToken(userToken.token)
            } catch (e: Exception) {
                Log.e("AuthVM", "Authorization error", e)
            }
        }
    }

    fun login() {
        _isAuthenticated.value = true
    }
}