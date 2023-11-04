package com.example.myapplication

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.databinding.ActivityMovieTicketingBinding
import com.example.myapplication.databinding.FragmentTicketingSeatBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class MovieTicketingActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityMovieTicketingBinding.inflate(layoutInflater)
    }

    var index : Int = -1
    lateinit var dbHelper: DBHelper



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val movieNm : String =  intent.getStringExtra("movieNm").toString()

        val movieOpenDt : String = intent.getStringExtra("movieOpenDt").toString()


        val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor

        editor.putString("movieNm",movieNm)
        editor.putString("movieOpenDt",movieOpenDt)
        editor.commit() // 필수






        binding.prevBtn.visibility = View.GONE

        binding.prevBtn.setOnClickListener {
            index -= 1
            if(index > 0) {
                binding.prevBtn.visibility = View.VISIBLE  //보임
            }else{
                binding.prevBtn.visibility = View.GONE  //안보임
            }
            ChangeFragment(index)
        }

        binding.nextBtn.setOnClickListener {
            binding.prevBtn.visibility = View.VISIBLE
            if(index == -1){
                index += 1
            }
            index += 1
            ChangeFragment(index)
        }

    }

    var mBackWait:Long = 0

    override fun onBackPressed() {


        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            Snackbar.make(binding.fragmentContainerView,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.",Snackbar.LENGTH_LONG).show()
        } else {

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

            finish() //액티비티 종료
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun ChangeFragment(idx : Int){

        val ticketingInfo = TicketingInfoFragment()   //예매 하려는 영화의 정보 및 동의사항
        val ticketingSeat = TicketingSeatFragment()  // 좌석 및 영화관 , 시간 선택
        val ticketingComplete = TicketingCompleteFragment()  // 결제 

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerView,ticketingSeat).commit()
        val transaction = supportFragmentManager.beginTransaction()

        val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)



        if(idx == 0){
            binding.progressBar.progress = 50
            transaction.replace(R.id.fragmentContainerView,ticketingSeat)
            binding.ticketingTitle.text = "예매"

            binding.nextBtn.visibility = View.VISIBLE
            binding.completedBtn.visibility = View.GONE
        }else if(idx == 1){

            //입력사항 체크
            if(prefs.getString("selectDate","오류").toString().equals("오류")  ||
                prefs.getString("selectTime","오류").toString().equals("오류") ||
                prefs.getString("selectSeat","오류").toString().length < 5) {

                //조건 식 좀 더 상세하게 필요  오류 문자열로 검열 불가

                Snackbar.make(binding.fragmentContainerView,"모든 항목을 선택해주세요.",Snackbar.LENGTH_LONG).show()
                index -= 1
                binding.prevBtn.visibility = View.GONE
                binding.nextBtn.visibility = View.VISIBLE
                binding.completedBtn.visibility = View.GONE

            }else {

                binding.progressBar.progress = 100
                transaction.replace(R.id.fragmentContainerView, ticketingComplete)
                binding.ticketingTitle.text = "결제"
                binding.nextBtn.visibility = View.GONE
                binding.completedBtn.visibility = View.VISIBLE
            }

        }else{
            println("INDEX 오류")
        }

        transaction.commit()


        binding.completedBtn.setOnClickListener {

            //예매 정보 db에 저장 작업
            val sdfNow = SimpleDateFormat("yyyy년 M월 dd일 HH : mm")
            val time = sdfNow.format(Date(System.currentTimeMillis()))


            dbHelper = DBHelper(this, "mydb.db", null, 1)
            dbHelper.insertTicketing(
                prefs.getString("userId","오류").toString()
                ,intent.getStringExtra("movieId").toString()
                ,prefs.getString("movieNm","오류").toString()
                ,prefs.getString("selectDate","오류").toString()
                ,prefs.getString("selectTime","오류").toString()
                ,prefs.getString("selectSeat","오류").toString()
                ,prefs.getInt("selectSeatCount",0)
                ,time)


            val build = AlertDialog.Builder(this)
            build.setTitle("예매 완료")
            build.setMessage(
                "예매 완료 되었습니다."
            )
            build.setPositiveButton("확인",
                    DialogInterface.OnClickListener{ dialog, id ->
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

                        val nextIntent = Intent(this,MovieActivity::class.java);
                        startActivity(nextIntent)
                    })
            build.setCancelable(false);
            build.show()



        }

    }

}