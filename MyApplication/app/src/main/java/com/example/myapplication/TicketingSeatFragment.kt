package com.example.myapplication

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale


class TicketingSeatFragment : Fragment(){

    lateinit var dbHelper: DBHelper

    @SuppressLint("ResourceAsColor", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val prefs : SharedPreferences = this.requireContext().getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor

        var view : View = inflater.inflate(R.layout.fragment_ticketing_seat, container, false)

        var SeatCheckBoxGroup : ArrayList<CheckBox> = ArrayList()

        for(i in 1..15) {
            SeatCheckBoxGroup.add(view.findViewWithTag<CheckBox>("seat" + i))
        }

        var selectDate : String = ""
        var selectTime : String = ""
        var selectSeat : ArrayList<String> = ArrayList()
        var selectSeatCount : Int = 0

        dbHelper = DBHelper(this.requireContext(), "mydb.db", null, 1)

        val ticketingDate : TextView =  view.findViewById<TextView>(R.id.ticketingDate)

        val btnArray = arrayOf( view.findViewById<ToggleButton>(R.id.ticketingTime),
            view.findViewById<ToggleButton>(R.id.ticketingTime2),
            view.findViewById<ToggleButton>(R.id.ticketingTime3),
            view.findViewById<ToggleButton>(R.id.ticketingTime4))

        if(prefs.getString("selectSeat",null).toString().length > 5 ){

            for(i in 0..14) {
                SeatCheckBoxGroup.get(i).isEnabled = true
            }

            val list : List<String> = prefs.getString("selectSeat", null)!!
                .replace("[","")
                .replace("]","")
                .replace(" ","")
                .split(",")


            list.forEach { x ->
                selectSeat.add(x)
                view.findViewWithTag<CheckBox>(x).isChecked = true
            }

        }

        if(prefs.getString("selectTime",null) != null){

            btnArray.forEach { btn ->
                btn.isEnabled = true

                if(btn.textOff.equals(prefs.getString("selectTime","asf"))){
                    btn.isChecked = true

                    val color = Color.parseColor("#FFCC0000") // 빨간색
                    btn.backgroundTintList = ColorStateList.valueOf(color)
                    btn.setTextColor(Color.WHITE)

                    }
                btn.setTextColor(Color.BLACK)
            }

            for(i in 0..14) {
                SeatCheckBoxGroup.get(i).isEnabled = true
            }

            val useSeatCursor : Cursor =  dbHelper.selectMovieSeat(prefs.getString("movieNm",null).toString()
                ,prefs.getString("selectDate","오류").toString()
                ,prefs.getString("selectTime","오류").toString())

            while(useSeatCursor.moveToNext()){

                val list : List<String> = useSeatCursor.getString(0)!!
                    .replace("[","")
                    .replace("]","")
                    .replace(" ","")
                    .split(",")


                list.forEach { x ->
                    view.findViewWithTag<CheckBox>(x).isEnabled = false
                }

            }



        }

        if(prefs.getString("selectDate",null) != null){
            ticketingDate.text = prefs.getString("selectDate",null)
        }




        for(i in 0..14) {
            SeatCheckBoxGroup.get(i).setOnCheckedChangeListener { compoundButton, b ->

                 val build = AlertDialog.Builder(this.requireContext())

                 if (b) {
                    build.setTitle("좌석 선택")
                    build.setMessage(
                        "" + compoundButton.tag + "좌석을 선택하셨습니다. "
                    )
                    build.setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, id ->
                            //배열 저장

                            selectSeat.add(compoundButton.tag.toString())
                            println("값 : " + selectSeat)
                            editor.putString("selectSeat",selectSeat.toString())
                            editor.putInt("selectSeatCount", prefs.getInt("selectSeatCount", 0) + 1)
                            editor.commit() // 필수

                        })

            .setCancelable(false);
    } else {

        build.setTitle("좌석 선택 취소")
        build.setMessage(
            "" + compoundButton.tag + "좌석이 취소되었습니다. "
        )
        build.setPositiveButton("확인",
            DialogInterface.OnClickListener { dialog, id ->
                //배열에서 삭제 처리
                selectSeat.remove(compoundButton.tag.toString())
                editor.putInt("selectSeatCount", prefs.getInt("selectSeatCount", 0) - 1)
                println("값 : " + selectSeat)

                editor.putString("selectSeat",selectSeat.toString())
                editor.commit() // 필수
            })
            .setCancelable(false);
    }

    build.show()

}
}

        ticketingDate.setOnClickListener(View.OnClickListener {view ->  //예매 날짜 선택


                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)

                val dlg = DatePickerDialog(this.requireContext(),
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                        var day = ""
                        if(dayOfMonth.toString().length == 1){
                            day = "0" + dayOfMonth
                        }else{
                            day = dayOfMonth.toString()
                        }

                        selectDate = "${year}년 ${month+1}월 ${day}일"
                        editor.putString("selectDate",selectDate)
                        editor.commit() // 필수


                        ticketingDate.text = selectDate

                        btnArray.forEach {toggleBtn ->

                            if( toggleBtn.isEnabled){

                                for(i in 0..14) {
                                    SeatCheckBoxGroup.get(i).isChecked = false
                                    SeatCheckBoxGroup.get(i).isEnabled = false

                                }// null포인트
                            }

                            //날짜를 변경 시 -> 두 뷰 요소 초기화

                            toggleBtn.isEnabled = true
                            val color = Color.parseColor("#ffffff")
                            toggleBtn.backgroundTintList = ColorStateList.valueOf(color)
                            toggleBtn.setTextColor(Color.BLACK)
                            toggleBtn.isChecked = false



                        }

                    }, year, month, date)


            val movieOpenDt : String = prefs.getString("movieOpenDt",null).toString()
            val OpenDtList : List<String> =  movieOpenDt.replace("개봉","").split("-")



            val dateString = movieOpenDt.replace("개봉","")
            val dateString2 = "$year-" + (month  + 1) + "-$date"  // 코드 변경 필요

            val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)

            val firstDate = format.parse(dateString)
            val secondDate = format.parse(dateString2)

            val calendar = Calendar.getInstance()


            when {
                firstDate.after(secondDate) -> {
                    calendar.set(Calendar.YEAR, Integer.parseInt(OpenDtList.get(0).replace(" ", "")))
                    calendar.set(Calendar.MONTH,Integer.parseInt(OpenDtList.get(1).replace(" ", "")) - 1)
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(OpenDtList.get(2).replace(" ", "")))
                    dlg.datePicker.minDate = calendar.timeInMillis

                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(OpenDtList.get(2).replace(" ", "")));
                }

                firstDate.before(secondDate) -> {
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, date)
                    dlg.datePicker.minDate = calendar.timeInMillis

                    calendar.set(Calendar.MONTH, month - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(OpenDtList.get(2).replace(" ", "")));

                }

                firstDate == secondDate -> {
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, date)
                    dlg.datePicker.minDate = calendar.timeInMillis

                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(OpenDtList.get(2).replace(" ", "")));
                }
            }


            // 영화 개봉 날짜 이후의 최대 날짜를 설정합니다.

            calendar.add(Calendar.DATE, 21);
            dlg.datePicker.maxDate = calendar.timeInMillis;



            dlg.show()
        })



        btnArray.forEach {toggleBtn -> toggleBtn.setOnClickListener(View.OnClickListener { //예매 시간 선택
            for(i in btnArray){
                if(i.id == toggleBtn.id){

                    val color = Color.parseColor("#FFCC0000") // 빨간색
                    i.backgroundTintList = ColorStateList.valueOf(color)
                    i.setTextColor(Color.WHITE)


                    i.isChecked = true
                    selectTime = i.textOff.toString()

                    editor.putString("selectTime",selectTime)
                    editor.commit() // 필수


                    for(i in 0..14) {
                        SeatCheckBoxGroup.get(i).isEnabled = true
                        SeatCheckBoxGroup.get(i).isChecked = false
                    }


                    val useSeatCursor : Cursor =  dbHelper.selectMovieSeat(prefs.getString("movieNm",null).toString()
                        ,prefs.getString("selectDate","오류").toString()
                        ,prefs.getString("selectTime","오류").toString())

                    while(useSeatCursor.moveToNext()){

                        val list : List<String> = useSeatCursor.getString(0)!!
                            .replace("[","")
                            .replace("]","")
                            .replace(" ","")
                            .split(",")


                        list.forEach { x ->
                            view.findViewWithTag<CheckBox>(x).isEnabled = false
                        }

                    }


                }else{
                    val color = Color.parseColor("#ffffff") // 빨간색
                    i.backgroundTintList = ColorStateList.valueOf(color)
                    i.setTextColor(Color.BLACK)
                    i.isChecked = false
                }
            }
            })
        }



        return view
    }

}