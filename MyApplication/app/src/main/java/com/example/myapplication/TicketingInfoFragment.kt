package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.databinding.ActivityMovieTicketingInfoBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TicketingInfoFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view : View = inflater.inflate(R.layout.fragment_ticketing_info, container, false)
        val prefs : SharedPreferences = this.requireContext().getSharedPreferences("Prefs", Context.MODE_PRIVATE)

//        view.findViewById<FloatingActionButton>(R.id.adultPlusBtn).setOnClickListener(View.OnClickListener {  //예매 날짜 선택
////            var adultTicketCount : Int = Integer.parseInt(view.findViewById<TextView>(R.id.adultTicketCount).text.toString())
//            view.findViewById<TextView>(R.id.adultTicketCount).text =
//                (Integer.parseInt(view.findViewById<TextView>(R.id.adultTicketCount).text.toString()) + 1).toString()
//
//        })





        view.findViewById<TextView>(R.id.fragmentMovieNm).text = prefs.getString("movieNm",null)


        // Inflate the layout for this fragment
        return view
    }



}