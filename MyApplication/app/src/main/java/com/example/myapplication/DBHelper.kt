package com.example.myapplication

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
): SQLiteOpenHelper(context, name, factory, version){


        override fun onCreate(db: SQLiteDatabase) {
        var sql: String = "CREATE TABLE if not exists userInfo(" +
                "userId text primary key," +
                "userNm text," +
                "userPw text," +
                "userPhone text," +
                "userEmail text," +
                "userDob text," +
                "userSex text," +
                "userSignUpDate text);"

        db.execSQL(sql)

        var sql2: String = "CREATE TABLE if not exists ticketing(" +
                "num INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId text," +
                "movieId text," +
                "movieNm text," +
                "movieDate text,"+
                "movieTime text,"+
                "selectSeat text,"+
                "totalAmount INTEGER,"+
                "ticketingDate text);"

        db.execSQL(sql2)


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql: String = "DROP TABLE if exists userInfo"
        db.execSQL(sql)
        onCreate(db)
    }

    fun userRegister(userId: String,userNm : String, userPw: String, userPhone: String,userEmail : String ,userDob : String , userSex : String, time : String) {
        val db = this.writableDatabase

        db.execSQL("INSERT INTO userInfo VALUES('$userId','$userNm', $userPw, '$userPhone', '$userEmail','$userDob','$userSex', '$time')")

    }

    fun userIdCheck(userId : String) : Boolean{
        val db = this.writableDatabase

        val cursor = db.rawQuery("SELECT * FROM userInfo where userId = '$userId'",null)

        println(cursor.count)


        return when{
            cursor.count > 0 -> false
            else -> true
        }

    }


    fun userLogin(userId:String, userPw:String) : Boolean{

        val db = this.writableDatabase

       val cursor = db.rawQuery("SELECT * FROM userInfo where userId = '$userId' and userPw = '$userPw'",null)

        return when{
            cursor.count == 0 -> false
            else -> true
        }
    }

    fun selectUserInfo(userId : String) : Cursor {
        val db = this.writableDatabase

        val cursor = db.rawQuery("SELECT * FROM userInfo where userId = '$userId'",null)
        val i : Int = 0
        cursor.moveToFirst()


        return cursor
    }

    fun selectTicketingInfo(userId : String) : Cursor{
        val db = this.writableDatabase

        val cursor = db.rawQuery("SELECT * FROM ticketing where userId = '$userId' order by num desc",null)

        return cursor

    }

    fun insertTicketing(userId : String,movieId : String, movieNm : String, movieDate : String, movieTime : String, selectSeat : String, totalAmount : Int,ticketingDate : String){
        val db = this.writableDatabase

        db.execSQL("INSERT INTO ticketing('userId','movieId','movieNm','movieDate','movieTime','selectSeat','totalAmount','ticketingDate') " +
                "VALUES('$userId','$movieId','$movieNm', '$movieDate', '$movieTime','$selectSeat','$totalAmount','$ticketingDate') ")

    }

    fun selectMovieSeat(movieNm : String, movieDate : String, movieTime : String) : Cursor{
        val db = this.writableDatabase

        val cursor = db.rawQuery("SELECT selectSeat FROM ticketing where movieNm = '$movieNm' and movieDate = '$movieDate' and movieTime = '$movieTime' ",null)

        return cursor



    }







}