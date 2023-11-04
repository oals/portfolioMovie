package com.example.myapplication

import android.R
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMovieTicketingInfoBinding


class MovieTicketingInfoActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityMovieTicketingInfoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)


//        txtText = findViewById<View>(R.id.txtText) as TextView
//
//        val intent = intent
//        val data = intent.getStringExtra("data")
//        txtText.setText(data)

    }
}