package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.screenHomepage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This enables the app to draw behind the system bars.
        enableEdgeToEdge()

        setContent {
            AlleyMateTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // This is your main UI screen
                    HomePage()
                }
            }
        }
    }
}