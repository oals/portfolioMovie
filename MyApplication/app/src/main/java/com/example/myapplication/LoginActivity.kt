package com.example.myapplication

import android.content.ClipData
import android.content.ClipData.Item
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val build = AlertDialog.Builder(this)
        build.setTitle("로그인 실패")
        build.setMessage(
            "아이디 혹은 비밀번호를 확인해주세요."
        )
        build.setPositiveButton("확인",
            DialogInterface.OnClickListener{ dialog, id ->
            })

        binding.loginChkBtn.setOnClickListener{

            val userId = binding.userId.text.toString()
            val userPw = binding.userPw.text.toString()

            if(userId.length == 0 || userPw.length == 0){
                build.show()
            }else{
                //db와 연결 후 로그인 처리

                dbHelper = DBHelper(this, "mydb.db", null, 1)
               val loginChk : Boolean =  dbHelper.userLogin(userId,userPw)

                if(loginChk){ //로그인 성공시
                    println("로그인 성공")

                    val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
                    val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor

                    editor.putString("userId",userId)
                    editor.commit() // 필수

                    val nextIntent = Intent(this,MovieActivity::class.java)
                    startActivity(nextIntent)

                }else{  //로그인 실패시
                    build.show()
                }


            }



        }

    }
}