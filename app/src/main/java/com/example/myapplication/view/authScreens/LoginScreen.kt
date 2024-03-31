package com.example.myapplication.view.authScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.models.AutoresizedText
import com.example.myapplication.view.camerasBookNProfile.itemsInCameras.TextFieldCustom
import com.example.myapplication.view.navigation.AuthScreen
import com.example.myapplication.view.navigation.GraphRoute

@Composable
fun LoginScreen(navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize().padding(start = 12.dp, end = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AutoresizedText(
            stringResource(R.string.sign_in_title),
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(top = 120.dp, bottom = 80.dp)
        )

        TextFieldCustom(stringResource(R.string.sign_in_email))
        Spacer(modifier = Modifier.height(15.dp))
        TextFieldCustom(stringResource(R.string.sign_in_password))

        Text(
            stringResource(R.string.sign_in_forgotpass),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding (top = 10.dp, bottom = 80.dp)
                .clickable { navController.navigate(AuthScreen.Forgot.route) }
        )
        Button(
            onClick = { // забывать про предыдущий экран
                navController.navigate(GraphRoute.MAIN)
                },
            shape = RoundedCornerShape(30),
            modifier = Modifier.width(140.dp).height(70.dp)
        ) {
            AutoresizedText(
                stringResource(R.string.sign_in_button),
                style = MaterialTheme.typography.labelMedium
            )
        }

        Row (
            modifier = Modifier.padding(top = 40.dp),
        ) {
            AutoresizedText(
                stringResource(R.string.sign_in_text_to_signup),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(end = 5.dp)
            )

            Text(stringResource(R.string.sign_in_tb_to_signup),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.navigate(AuthScreen.Registration.route)
                }
            )
        }

    }

}