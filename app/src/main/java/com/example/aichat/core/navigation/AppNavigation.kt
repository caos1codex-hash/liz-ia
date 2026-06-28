package com.example.aichat.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.aichat.ui.screens.ChatScreen
import com.example.aichat.ui.screens.HomeScreen
import com.example.aichat.ui.screens.SettingsScreen

/**
 * Routes centrales de la app.
 */
object Routes {
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val CHAT = "chat/{chatId}"

    fun chat(chatId: Long): String = "chat/$chatId"
}

/**
 * NavHost principal.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onOpenChat = { chatId -> navController.navigate(Routes.chat(chatId)) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                onCreateChat = { chatId -> navController.navigate(Routes.chat(chatId)) }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.CHAT,
            arguments = listOf(navArgument("chatId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getLong("chatId") ?: -1L
            ChatScreen(
                chatId = chatId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
