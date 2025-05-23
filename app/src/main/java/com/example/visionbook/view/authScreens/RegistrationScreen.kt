package com.example.visionbook.view.authScreens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.visionbook.R
import com.example.visionbook.models.AutoresizedText
import com.example.visionbook.models.api.AuthApi
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.BackButton
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.TextFieldEmail
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.TextFieldPass
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.TextFieldFio
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.TextFieldDep
import com.example.visionbook.view.navigation.GraphRoute
import com.example.visionbook.viewmodels.AuthVM
import com.example.visionbook.viewmodels.RetrofitVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Thread.sleep



@Composable
fun RegistrationScreen(
    navController: NavController,
    authViewModel: AuthVM,
    retrofitViewModel: RetrofitVM = viewModel()
) {
    val context = LocalContext.current
    val authApi = retrofitViewModel.retrofit.create(AuthApi::class.java)
    val fioState = remember { mutableStateOf("") }
    val depState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val secondPasswordState = remember { mutableStateOf("") }
    var passwordsMatchState by remember { mutableStateOf(false) }

    val checkEmailPass = stringResource(R.string.check_email_pass)
    val checkPrivacyPolicy = stringResource(R.string.check_privacypolicy)
    val matchPass = stringResource(R.string.match_pass)
    DisposableEffect(authViewModel) {

        val observerFioState = Observer<String> { _fioState ->
            fioState.value = _fioState
        }
        authViewModel.fioState.observeForever(observerFioState)

        val observerDepState = Observer<String> { _depState ->
            depState.value = _depState
        }
        authViewModel.depState.observeForever(observerDepState)

        val observerEmailState = Observer<String> { _emailState ->
            emailState.value = _emailState
        }
        authViewModel.emailState.observeForever(observerEmailState)

        val observerPasswordState = Observer<String> { _passwordState ->
            passwordState.value = _passwordState
        }
        authViewModel.passwordState.observeForever(observerPasswordState)

        val observerSecondPasswordState = Observer<String> { _secondPasswordState ->
            secondPasswordState.value = _secondPasswordState
        }
        authViewModel.secondPasswordState.observeForever(observerSecondPasswordState)
        onDispose {
            authViewModel.fioState.removeObserver(observerFioState)
            authViewModel.depState.removeObserver(observerDepState)
            authViewModel.emailState.removeObserver(observerEmailState)
            authViewModel.passwordState.removeObserver(observerPasswordState)
            authViewModel.secondPasswordState.observeForever(observerSecondPasswordState)
        }
    }
    var checked by remember {
        mutableStateOf(false)
    }
    val updatePasswordMatchState: () -> Unit = {
        passwordsMatchState = authViewModel.checkPasswordMatch(passwordState.value, secondPasswordState.value)
    }

    Column(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp)
        ) { BackButton(navController) }

        AutoresizedText(
            stringResource(R.string.sign_up_title),
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(bottom = 60.dp)
        )

        TextFieldFio(
            stringResource(R.string.sign_in_fio),
            fioState,
            onValueChange = { newValue -> fioState.value = newValue })
        Spacer(modifier = Modifier.height(15.dp))
        TextFieldDep(
            stringResource(R.string.sign_in_dep),
            depState,
            onValueChange = { newValue -> depState.value = newValue })
        Spacer(modifier = Modifier.height(15.dp))
        TextFieldEmail(
            stringResource(R.string.sign_in_email),
            emailState,
            onValueChange = { newValue -> emailState.value = newValue })
        Spacer(modifier = Modifier.height(15.dp))
        TextFieldPass(
            stringResource(R.string.sign_in_password),
            passwordState = passwordState,
            secondPasswordState = secondPasswordState,
            onValueChange = { newValue -> passwordState.value = newValue }
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextFieldPass(
            stringResource(R.string.sign_in_retrypassword),
            secondPassword = true,
            passwordState,
            secondPasswordState = secondPasswordState,
            onValueChange = {
                    newValue -> secondPasswordState.value = newValue
                updatePasswordMatchState()
            },
        )


        Row(
            modifier = Modifier.padding(start = 30.dp, top = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = { _checked ->
                    checked = _checked
                },
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 10.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF4262A2),       // Цвет галочки в активном состоянии
                    uncheckedColor = Color(0xFF4262A2),     // Цвет рамки в неактивном состоянии
                    checkmarkColor = Color.White     // Цвет галочки
            )
            )
            Text(
                stringResource(R.string.sign_up_confidence),
                style = MaterialTheme.typography.titleSmall
            )
        }
        Button(
            onClick = {
                if (passwordsMatchState && passwordState.value != "" && emailState.value != "" && checked) {
                    CoroutineScope(Dispatchers.IO).launch {
                        authViewModel.registration(
                            emailState.value, passwordState.value, authApi.toString(),
                            password = TODO(),
                            authApi = TODO()
                        )
                        sleep(1000)
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        authViewModel.authorization(
                            emailState.value, passwordState.value, authApi.toString(),
                            password = TODO(),
                            authApi = TODO()
                        )
                    }
                    navController.navigate(GraphRoute.MAIN) {
                        navController.popBackStack(GraphRoute.LOGIN, true)
                    }
                } else if (passwordState.value == "" || emailState.value == "") {
                    Toast.makeText(
                        context,
                        checkEmailPass,
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (checked == false) {
                    Toast.makeText(
                        context,
                        checkPrivacyPolicy,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        matchPass,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            shape = RoundedCornerShape(30),
            modifier = Modifier
                .width(140.dp)
                .height(80.dp)
                .padding(top = 30.dp),

            ) {
            AutoresizedText(
                stringResource(R.string.sign_in_tb_to_signup),
                style = MaterialTheme.typography.labelMedium
            )
        }

    }
}