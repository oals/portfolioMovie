package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView


class TicketingCompleteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view : View = inflater.inflate(R.layout.fragment_ticketing_complete, container, false)
        val prefs : SharedPreferences = this.requireContext().getSharedPreferences("Prefs", Context.MODE_PRIVATE)

        view.findViewById<TextView>(R.id.completeMovieNm).text =   "- "+ prefs.getString("movieNm",null)

        view.findViewById<TextView>(R.id.completeSelectDate).text = "- "+ prefs.getString("selectDate",null)

        view.findViewById<TextView>(R.id.completeSelectTime).text = prefs.getString("selectTime",null)

//        view.findViewById<TextView>(R.id.completeSelectSeat).text = "선택한 좌석 : " + prefs.getString("selectSeat","asd")

        val list : List<String> = prefs.getString("selectSeat","asd").toString()
            .replace("[","")
            .replace("]","")
            .replace(" ","")
            .split(",")


        list.forEach { x ->
            view.findViewWithTag<CheckBox>(x).isChecked = true
        }




        view.findViewById<TextView>(R.id.selectSeatCount).text =
            "(선택한 좌석 수) " + " X 9000원"

        view.findViewById<TextView>(R.id.totalAmount).text = "총 결제 금액 : " +  prefs.getInt("selectSeatCount",0) * 9000 + "원"



        return view

    }

}