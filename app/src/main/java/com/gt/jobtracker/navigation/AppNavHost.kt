package com.gt.jobtracker.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.gt.jobtracker.feature.applications.ApplicationsScreen
import com.gt.jobtracker.flags.FeatureFlagManager

@Composable
fun AppNavHost(
    navController: NavHostController,
    featureFlagManager: FeatureFlagManager
) {
    NavHost(
        navController = navController,
        startDestination = ApplicationsRoute
    ) {
        composable<ApplicationsRoute>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "jobtracker://applications"
                    action = Intent.ACTION_VIEW
                }
            )
        ) {
            ApplicationsScreen(
                onNavigateToDetail = { applicationId ->
                    navController.navigate(ApplicationDetailRoute(applicationId))
                },
                onNavigateToAddEdit = {
//                    throw RuntimeException("Test Crash") // Force a crash
                    navController.navigate(AddEditRoute())
                }
            )
        }

        composable<ApplicationDetailRoute>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "jobtracker://applications/{applicationId}"
                    action = Intent.ACTION_VIEW
                }
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<ApplicationDetailRoute>()
            com.gt.jobtracker.feature.applications.detail.DetailScreen(
                applicationId = route.applicationId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id ->
                    navController.navigate(AddEditRoute(applicationId = id))
                }
            )
        }

        composable<AddEditRoute>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "jobtracker://addedit"
                    action = Intent.ACTION_VIEW
                }
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<AddEditRoute>()
            com.gt.jobtracker.feature.addedit.AddEditScreen(
                applicationId = route.applicationId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}