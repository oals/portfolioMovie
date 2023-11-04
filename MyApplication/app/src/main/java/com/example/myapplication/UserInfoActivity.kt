package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.TextView
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.ActivityUserInfoBinding

class UserInfoActivity : AppCompatActivity() {

    lateinit var dbHelper: DBHelper

    private val binding by lazy {
        ActivityUserInfoBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)


        dbHelper = DBHelper(this, "mydb.db", null, 1)

        val userInfoCursor : Cursor =  dbHelper.selectUserInfo(prefs.getString("userId",null).toString())

        if(prefs.getString("userId",null) != null) {

            binding.myPageUserId.text = Editable.Factory.getInstance().newEditable(userInfoCursor.getString(0))
            binding.myPageUserNm.text = Editable.Factory.getInstance().newEditable(userInfoCursor.getString(1))
            binding.myPageUserPhone.text = Editable.Factory.getInstance().newEditable(userInfoCursor.getString(3))
            binding.myPageUserEmail.text = Editable.Factory.getInstance().newEditable(userInfoCursor.getString(4))
            binding.myPageUserDob.text = Editable.Factory.getInstance().newEditable(userInfoCursor.getString(5))
            binding.myPageUserSex.text = Editable.Factory.getInstance().newEditable(userInfoCursor.getString(6))
            binding.myPageUserSignUpDate.text = Editable.Factory.getInstance().newEditable(userInfoCursor.getString(7))


            if(binding.myPageUserSex.text.toString() == "남자"){
                binding.manImage.visibility = View.VISIBLE
                binding.womanImage.visibility = View.GONE

            }else{
                binding.womanImage.visibility = View.VISIBLE
                binding.manImage.visibility = View.GONE
            }



        }


        binding.backBtn.setOnClickListener {
            finish()
        }



    }
}