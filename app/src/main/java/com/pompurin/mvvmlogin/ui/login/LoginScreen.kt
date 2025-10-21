package com.pompurin.mvvmlogin.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pompurin.mvvmlogin.R
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    navigateToHome: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Login(Modifier.align(Alignment.Center), viewModel, { navigateToHome() })
    }
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel, navigateToHome: () -> Unit) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val loginEnable : Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderImage()
            Spacer(modifier = Modifier.padding(16.dp))
            EmailField(email, { viewModel.onLoginChanged(it, password) })
            Spacer(modifier = Modifier.padding(8.dp))
            PasswordField(password, { viewModel.onLoginChanged(email, it)})
            Spacer(modifier = Modifier.padding(16.dp))
            ForgotPassword()
            Spacer(modifier = Modifier.padding(8.dp))
            LoginButton(
                loginEnable,
                { navigateToHome() }
                //{coroutineScope.launch { viewModel.onLoginSelected() }}
            )
        }
    }
}

@Composable
fun LoginButton(loginEnable: Boolean, navigateToHome: () -> Unit) {
    Button(
        //onClick = { onLoginSelected() },
        onClick = { navigateToHome() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.primary),
            contentColor = colorResource(R.color.onSurface)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        ),
        enabled = loginEnable
    ) {
        Text(
            text = "Login",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ForgotPassword() {
    Text(
        text = "Forgot your password?",
        modifier = Modifier.clickable{},
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = colorResource(R.color.primary)
    )
}

@Composable
fun PasswordField(password: String, onTextFieldChanged:(String) -> Unit) {

    TextField(
        value = password,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Password", color = colorResource(R.color.primary)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = colorResource(R.color.primary),          // Azul marino muy oscuro
            unfocusedTextColor = colorResource(R.color.primary),
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = colorResource(R.color.primary),     // Azul marino
            unfocusedIndicatorColor = colorResource(R.color.primary),   // Gris azulado claro
            cursorColor = colorResource(R.color.primary),
        )
    )
}

@Composable
fun EmailField(email: String, onTextFieldChanged:(String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email", color = colorResource(R.color.primary)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = colorResource(R.color.primary),
            unfocusedTextColor = colorResource(R.color.primary),
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = colorResource(R.color.primary),
            unfocusedIndicatorColor = colorResource(R.color.primary),
            cursorColor = colorResource(R.color.primary),
        )
    )
}

@Composable
fun HeaderImage() {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "Header",
        modifier = Modifier
            .size(120.dp)
            .padding(8.dp)
    )
}