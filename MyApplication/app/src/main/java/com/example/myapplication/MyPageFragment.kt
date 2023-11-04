package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ListView
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONObject


class MyPageFragment : Fragment() {

    lateinit var dbHelper: DBHelper
    var queue : RequestQueue? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var view: View = inflater.inflate(R.layout.fragment_my_page, container, false)


        view.findViewById<Button>(R.id.userInfoBtn).setOnClickListener {

            val intent = Intent(activity, UserInfoActivity::class.java)
            startActivity(intent)

        }


        view.findViewById<Button>(R.id.ticketingListBtn).setOnClickListener {
            val intent = Intent(activity, TicketingListActivity::class.java)
            startActivity(intent)

        }





        return view
    }
}