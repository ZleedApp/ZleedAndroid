package com.zleed.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.zleed.app.classes.ZleedSingleton
import org.json.JSONObject

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val loginActivityView: ConstraintLayout = findViewById(R.id.loginActivityView)

        val textFieldEmailInput: TextInputEditText = findViewById(R.id.textFieldEmailInput)
        val textFieldPasswordInput: TextInputEditText = findViewById(R.id.textFieldPasswordInput)

        val loginButton: Button = findViewById(R.id.loginButton)

        val i1 = Intent().setClass(this, MainActivity::class.java)

        loginButton.setOnClickListener {
            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, "https://zleed.ga/api/v1/auth/login", JSONObject("{\"email\":\"${textFieldEmailInput.text}\",\"password\":\"${textFieldPasswordInput.text}\"}"),
                { response ->
                    val responseJsonObject = JSONObject(response.toString())

                    Log.d("LoginRequestSuccess", responseJsonObject.getString("messageCode"))

                    if(responseJsonObject.getInt("status") == 0) {
                        if(responseJsonObject.getString("messageCode") == "PASSWORD_INVALID")
                            textFieldPasswordInput.error = "Invalid Password."
                        else if(responseJsonObject.getString("messageCode") == "EMAIL_INVALID")
                            textFieldEmailInput.error = "Invalid E-Mail."
                        else {
                            textFieldEmailInput.error = "Something Went Wrong."
                            textFieldPasswordInput.error = "Something Went Wrong."

                            Snackbar.make(loginActivityView, "Something Went Wrong", Snackbar.LENGTH_SHORT).show()
                        }
                    } else {
                        val dataJsonObject = responseJsonObject.getJSONObject("data")

                        val sharedPreferences       = getSharedPreferences("ZleedAppData", MODE_PRIVATE)
                        val sharedPreferencesEditor = sharedPreferences.edit()

                        sharedPreferencesEditor.putBoolean("isLoggedIn", true)
                        sharedPreferencesEditor.putString("jwtToken", dataJsonObject.getString("jwtToken"))
                        sharedPreferencesEditor.putInt("jwtExpires", dataJsonObject.getInt("jwtExpires"))

                        sharedPreferencesEditor.apply()

                        startActivity(i1)
                        finish()
                    }
                },
                { error ->
                    Log.d("LoginRequestError", error.toString())

                    Snackbar.make(loginActivityView, "Login Unsuccessful", Snackbar.LENGTH_SHORT)
                        .setAction("Ok") {
                            Log.d("LoginRequestErrorActionOk", "OK")
                        }.show()
                }
            )

            ZleedSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        }
    }
}