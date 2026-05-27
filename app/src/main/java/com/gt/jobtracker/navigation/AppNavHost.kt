package com.gt.jobtracker.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
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
        ) {
            androidx.compose.material3.Text("Detail Screen — Coming Soon")
        }

        composable<AddEditRoute>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "jobtracker://addedit"
                    action = Intent.ACTION_VIEW
                }
            )
        ) {
            androidx.compose.material3.Text("Add/Edit Screen — Coming Soon")
        }
    }
}