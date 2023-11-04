package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityTicketingListBinding
import org.json.JSONObject

class TicketingListActivity : AppCompatActivity() {

    lateinit var dbHelper: DBHelper
    var queue: RequestQueue? = null

    private val binding by lazy {
        ActivityTicketingListBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent = Intent(this, MovieInfoActivity::class.java)
        val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)

        dbHelper = DBHelper(this, "mydb.db", null, 1)

        if (queue == null) {
            queue = Volley.newRequestQueue(this)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }


        val ticketingCursor: Cursor =
            dbHelper.selectTicketingInfo(prefs.getString("userId", null).toString())


        val ticketingList: ArrayList<Ticket> = ArrayList()



        while (ticketingCursor.moveToNext()) {

            val ticket: Ticket = Ticket(
                ticketingCursor.getString(1),
                ticketingCursor.getString(2),
                ticketingCursor.getString(3),
                ticketingCursor.getString(4),
                ticketingCursor.getString(5),
                ticketingCursor.getString(6),
                ticketingCursor.getInt(7),
                ticketingCursor.getString(8)
            )

            ticketingList.add(ticket)
        }


        val adapter = TicketingAdapter(this, ticketingList)
        binding.ticketingList.adapter = adapter



        binding.ticketingList.setOnItemClickListener { adapterView, view, i, l ->
                val clickMovie = ticketingList[i]


                val build = AlertDialog.Builder(this).setView(R.layout.activity_movie_ticketing_info)
                val dialog = build.create()

                GetOtherMovieInfo(clickMovie.movieId, dialog)

                dialog.setOnShowListener {
                    dialog.findViewById<TextView>(R.id.ticketMovieNm)?.text = clickMovie.movieNm

                    dialog.findViewById<TextView>(R.id.ticketTotalAmount)?.text =
                        "결제 금액 : " + (clickMovie.selectSeatCount * 9000) + "원"

                    dialog.findViewById<TextView>(R.id.ticketMovieDate)?.text =
                        "결제일 : " + clickMovie.ticketingDate

                    dialog.findViewById<TextView>(R.id.ticketDate)?.text =
                        "예매일 : " + clickMovie.selectDate + " " + clickMovie.selectTime



                    val list: List<String> = clickMovie.selectSeat
                        .replace("[", "")
                        .replace("]", "")
                        .replace(" ", "")
                        .split(",")


                    list.forEach { x ->
                        dialog.findViewById<TableLayout>(R.id.tableLayout)
                            ?.findViewWithTag<CheckBox>(x)?.isChecked = true
                    }

                    dialog.findViewById<Button>(R.id.closeBtn)?.setOnClickListener { v ->
                        //창 닫기
                        dialog.dismiss()
                    }

                }

                val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialog.window?.attributes)
                lp.width = 1450
                lp.height = 1700
                dialog.window?.attributes = lp

                dialog.show()


            }




    }



    private fun GetOtherMovieInfo(movieId: String, root: AlertDialog) {

        val url =
            " https://api.themoviedb.org/3/movie/${movieId}?api_key=b30e9c35557f3ffb06960709c88d144b&language=ko-Kr"
        val jsonRequest: JsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, url, null, { response ->

                val jsonObject: JSONObject = response
                val defaultImage = R.drawable.android

                root.findViewById<TextView>(R.id.ticketMovieRunTime)?.text = jsonObject.get("runtime").toString() + "분"
                root.findViewById<TextView>(R.id.ticketMovieOpenDt)?.text = jsonObject.get("release_date").toString() + " 개봉"

                root.findViewById<ImageView>(R.id.ticketMovieImg)?.let {
                    Glide.with(this)
                        .load(
                            "https://image.tmdb.org/t/p/w500" + jsonObject.get("poster_path")
                        ) // 불러올 이미지 url
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                        .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(it)
                }


            }, { error ->
                println(error.printStackTrace())
            }
            )
        queue?.add(jsonRequest)
    }


}