package com.gt.jobtracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.gt.jobtracker.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }

//        test Crash
//        setContent {
//            MaterialTheme {
//                val navController = rememberNavController()
//                Box(modifier = Modifier.fillMaxSize()) {
//                    AppNavHost(navController = navController)
//                    Button(
//                        onClick = { throw RuntimeException("Test crash — Crashlytics verification") },
//                        modifier = Modifier.align(Alignment.BottomCenter)
//                    ) {
//                        Text("Test Crash")
//                    }
//                }
//            }
//        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }
}