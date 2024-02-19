package com.tim.currencyexchanger.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tim.currencyexchanger.ui.auth.SignInScreen
import com.tim.currencyexchanger.ui.currencyExchang.CurrencyExchangerScreen
import com.tim.currencyexchanger.ui.theme.CurrencyExchangerTheme
import com.tim.currencyexchanger.utils.NavigationDispatcher
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationDispatcher: NavigationDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyExchangerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    LaunchedEffect(key1 = navController) {
                        navController.observeNavigationCommands()
                    }

                    NavHost(navController = navController, startDestination = "signIn") {
                        composable("signIn") {
                            SignInScreen()
                        }
                        composable("currencyExchanger/{userId}/{userAge}?timId={timId}&setNumber={setNumber}") {
                            CurrencyExchangerScreen(navController = navController)
                        }
                        composable("home") {
                            HomeScreen(navController)
                        }
                    }
                }
            }
        }
    }

    private suspend fun NavController.observeNavigationCommands() {
        for (command in navigationDispatcher.navigationEmitter) {
            command.invoke(this)
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = "Hello",
        textAlign = TextAlign.Center,
        color = Color.Red
    )
}