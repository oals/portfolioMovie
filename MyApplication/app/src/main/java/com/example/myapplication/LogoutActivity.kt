package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LogoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)


        val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("userId")
        editor.commit()

        val nextIntent = Intent(this,MovieActivity::class.java)
        startActivity(nextIntent)




    }
}