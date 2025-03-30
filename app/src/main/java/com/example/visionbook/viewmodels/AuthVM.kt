package com.example.visionbook.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visionbook.models.api.AuthApi
import com.example.visionbook.models.dataclasses.RegistrationModel
import kotlinx.coroutines.Dispatchers
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


    private val _tokenState = MutableLiveData<String>()
    val tokenState: LiveData<String> = _tokenState

    private val _isAuthenticated = MutableLiveData<Boolean>(false)
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    fun loginSuccess(token: String) {
        setToken(token)
        _isAuthenticated.value = true
    }

    fun logout() {
        // Очищаем все данные аутентификации
        _tokenState.value = ""
        _isAuthenticated.value = false
        _emailState.value = ""
        _passwordState.value = ""
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
                withContext(Dispatchers.Main) {
                    authApi.userRegistration(
                        RegistrationModel(
                            fio,
                            dep,
                            email,
                            password
                        )
                    )
                }
            } catch (e: Exception) {
                // Обработка ошибок
                e.printStackTrace()
            }
        }
    }

    suspend fun authorization(fio: String, dep: String, email: String, password: String, authApi: AuthApi) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                withContext(Dispatchers.Main) {
                    val userToken = authApi.userLogin(
                        RegistrationModel(
                            fio,
                            dep,
                            email,
                            password
                        )

                    )
                    Log.d("ВЕРНИ ТОКЕН ЗАРАЗА", userToken.token)
                    // Установка токена после успешной аутентификации
                    setToken(userToken.token)
                }
            } catch (e: Exception) {
                // Обработка ошибок
                e.printStackTrace()
            }
        }
    }


}