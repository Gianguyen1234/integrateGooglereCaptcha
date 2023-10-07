package com.example.recaptchaapplication


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.recaptchaapplication.captcha.Recaptcha
import com.example.recaptchaapplication.ui.theme.ReCAPTCHAApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReCAPTCHAApplicationTheme{
                // on below line specifying theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        // specifying top bar on below line
                        topBar = {
                            // adding background color for top bar.
                            TopAppBar(backgroundColor = Color.DarkGray,
                                // adding title for top bar on below line
                                title = {
                                    // adding text in our top bar on below line,
                                    Text(
                                        // adding text style, text
                                        // and alignment for text
                                        text = "reCAPTCHA in Android",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.Cyan
                                    )
                                })
                        }) {
                        // calling method
                        // to display UI,
                        Recaptcha()
                    }
                }
            }
        }
    }
}



