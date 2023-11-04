package com.example.myapplication

import android.R
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import java.util.Timer
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {


    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val SPLASH_TIME_OUT:Long = 4000

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor

        if(prefs.getString("movieNm",null) != null){
            editor.remove("movieNm")
        }

        if(prefs.getString("selectSeat",null) != null){
            editor.remove("selectSeat")
        }

        if(prefs.getString("selectTime",null) != null){
            editor.remove("selectTime")
        }

        if(prefs.getString("selectDate",null) != null){
            editor.remove("selectDate")
        }
        if(prefs.getInt("selectSeatCount",0) != 0){
            editor.remove("selectSeatCount")
        }

        editor.commit()


        Handler().postDelayed(Runnable{
            kotlin.run {
                val nextIntent = Intent(this, MovieActivity::class.java)
                startActivity(nextIntent)
                finish()
            }

        }, SPLASH_TIME_OUT)



    }
}