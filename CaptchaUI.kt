package com.example.recaptchaapplication.captcha

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import org.json.JSONObject
import java.util.HashMap

// creating a composable for displaying UI.
@Composable
fun Recaptcha() {

    // creating a variable for context
    val ctx = LocalContext.current

    // creating a variable for site key.

    // creating a column for
    // displaying a text and a button.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth(),

        // specifying vertical and horizontal alignment.
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // specifying text for a text view
        Text(
            text = "reCAPTCHA in Android",
            color = Color.Cyan,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
        )
        // adding a spacer on below line.
        Spacer(modifier = Modifier.height(5.dp))

        // creating a button to verify the user.
        Button(
            // adding on click for the button.
            onClick = {
                // calling safety net on below line to verify recaptcha.
                SafetyNet.getClient(ctx).verifyWithRecaptcha("6Le6TE0kAAAAADIvgsRb-deegpCHK30f2ifjILOh").addOnSuccessListener {
                    if (it.tokenResult!!.isNotEmpty()) {
                        // calling handle verification method
                        // to handle verification.
                        handleVerification(it.tokenResult.toString(), ctx)
                    }
                }.addOnFailureListener {
                    // on below line handling exception
                    if (it is ApiException) {
                        val apiException = it
                        // below line is use to display an
                        // error message which we get.
                        Log.d(
                            "TAG", "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.statusCode)
                        )
                    } else {
                        // below line is use to display a toast message for any error.
                        Toast.makeText(ctx, "Error found is : $it", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            },
            // on below line adding
            // modifier for our button.
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // on below line specifying text for button.
            Text(text = "Verify Captcha", modifier = Modifier.padding(8.dp))
        }
    }
}
// creating a method to handle verification.
fun handleVerification(responseToken: String, ctx: Context) {
    // inside handle verification method we are
    // verifying our user with response token.
    // url to sen our site key and secret key
    // to below url using POST method.
    val url = "https://www.google.com/recaptcha/api/siteverify"

    // creating a new variable for our request queue
    val queue = Volley.newRequestQueue(ctx)
    val secretKey = "Enter your secret key"

    val request =
        object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                // inside on response method we are checking if the
                // response is successful or not.
                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getBoolean("success")) {
                        // if the response is successful
                        // then we are showing below toast message.
                        Toast.makeText(
                            ctx,
                            "User verified with reCAPTCHA",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // if the response if failure
                        // we are displaying
                        // a below toast message.
                        Toast.makeText(
                            ctx,
                            jsonObject.getString("error-codes").toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (ex: Exception) {
                    // if we get any exception then we are
                    // displaying an error message in logcat.
                    Log.d("TAG", "JSON exception: " + ex.message)
                }
            }, Response.ErrorListener { // displaying toast message on response failure.
                Toast.makeText(ctx, "Fail to post data..", Toast.LENGTH_SHORT)
                    .show()
            }) {
            override fun getParams(): Map<String, String> {

                val params: MutableMap<String, String> = HashMap()
                params["secret"] = secretKey
                params["response"] = responseToken
                return params
            }
        }
    // below line of code is use to set retry
    // policy if the api fails in one try.
    request.retryPolicy = DefaultRetryPolicy( // we are setting time for retry is 5 seconds.
        50000,  // below line is to perform maximum retries.
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    )
    // below line is to make
    // a json object request.
    queue.add(request)

}