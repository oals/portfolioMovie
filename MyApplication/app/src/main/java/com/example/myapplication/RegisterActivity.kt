package com.example.myapplication

import android.R
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRegisterBinding
import java.text.SimpleDateFormat
import java.util.Date


class RegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    lateinit var dbHelper: DBHelper
    var userSex : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




        binding.registerUserSexManBtn.setOnClickListener {

            userSex = "남자"
            if(binding.registerUserSexWomanBtn.isChecked){
                binding.registerUserSexWomanBtn.isChecked = false
            }
        }

        binding.registerUserSexWomanBtn.setOnClickListener {

            userSex = "여자"
            if(binding.registerUserSexManBtn.isChecked){
                binding.registerUserSexManBtn.isChecked = false
            }
        }


        binding.registerBtn.setOnClickListener {
            val userNm : String = binding.registerUserNm.text.toString()
            val userId : String = binding.registerUserId.text.toString()
            val userPw : String = binding.registerUserPw.text.toString()
            val userPhone1 : String = binding.registerUserPhone1.text.toString()
            val userPhone2 : String = binding.registerUserPhone2.text.toString()
            val userPhone3 : String = binding.registerUserPhone3.text.toString()

            val userEmail1 : String = binding.registerUserEmail1.text.toString()
            val userEmail2 : String = binding.registerUserEmail2.text.toString()

            val userPhone : String = "$userPhone1 - $userPhone2 - $userPhone3"
            val userEmail : String = "$userEmail1@$userEmail2"


            val build = AlertDialog.Builder(this)
            var errorMsg : String = ""

            val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$".toRegex()
            val isEmailValid = emailRegex.matches(userEmail)

            if(userId.length == 0 ||
                userPw.length == 0 ||
                userPhone.length != 17 ||
                !isEmailValid ||
                userNm.length == 0 ||
                userSex.length == 0 ){

                if(userId.length == 0){
                    errorMsg = "사용할 수 없는 아이디 입니다."
                }else if(userPw.length == 0 ){
                    errorMsg = "사용할 수 없는 비밀번호 입니다."
                }else if(userPhone.length != 17){
                    errorMsg = "전화번호 11자리를 확인해주세요."
                }else if(!isEmailValid){
                    errorMsg = "이메일 주소 $userEmail 은(는) 유효한 이메일 주소입니다: $isEmailValid"
                }else if(userNm.length == 0){
                    errorMsg = "사용할 수 없는 닉네임 입니다."
                }else if(userSex.length == 0){
                    errorMsg = "성별을 선택해주세요."
                }else{
                    errorMsg = "잠시후 다시 시도해주세요."
                }

                build.setTitle("회원가입 실패")
                build.setMessage(errorMsg)
                build.setPositiveButton("확인",
                    DialogInterface.OnClickListener{ dialog, id ->

                    })
                build.setCancelable(false);


            }else{

                dbHelper = DBHelper(this, "mydb.db", null, 1)

                val userIdChk = dbHelper.userIdCheck(userId)

                if(userIdChk) {
                    val nextIntent = Intent(this,LoginActivity::class.java)

                    val sdfNow = SimpleDateFormat("yyyy년 MM월 dd일")
                    val time = sdfNow.format(Date(System.currentTimeMillis()))

                    val spinner = binding.dpSpinner
                    val selectedValue =  spinner.year.toString() + "." + (Integer.parseInt(spinner.month.toString()) + 1)  + "." + spinner.dayOfMonth


                    dbHelper.userRegister(userId,userNm,userPw,userPhone,userEmail,selectedValue,userSex, "$time 가입")

                    build.setTitle("회원가입 성공")
                    build.setMessage(
                        "회원가입이 완료되었습니다."
                    )
                    build.setPositiveButton("확인",
                        DialogInterface.OnClickListener{ dialog, id ->
                            startActivity(nextIntent)
                        })
                    build.setCancelable(false);

                }else{
                    build.setTitle("회원가입 실패")
                    build.setMessage(
                        "이미 존재하는 아이디 입니다.."
                    )
                    build.setPositiveButton("확인",
                        DialogInterface.OnClickListener{ dialog, id ->
                        })
                    build.setCancelable(false);
                }
            }
            build.show()



        }








    }
}