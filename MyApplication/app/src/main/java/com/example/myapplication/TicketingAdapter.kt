package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONObject

class TicketingAdapter(val context: Context, val ticketingList : ArrayList<Ticket>)  : BaseAdapter() {

    var queue: RequestQueue? = null

    override fun getCount(): Int {
        return ticketingList.size
    }

    override fun getItem(position: Int): Any {
        return ticketingList[position]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {

        val view : View = LayoutInflater.from(context).inflate(R.layout.list_ticket_item,null)

        if (queue == null) {
            queue = Volley.newRequestQueue(this.context)
        }

        val movieNm = view.findViewById<TextView>(R.id.itemMovieNm)

        val ticket = ticketingList[position]

        if(ticket.movieId.isNotEmpty()) {
             GetOtherMovieInfoList(ticket.movieId,view)
        }

        movieNm.text = ticket.movieNm
        view.findViewById<TextView>(R.id.clickText).paintFlags = Paint.UNDERLINE_TEXT_FLAG

        return view
    }


    private fun GetOtherMovieInfoList(movieId: String, view: View) {


        val url =
            " https://api.themoviedb.org/3/movie/${movieId}?api_key=b30e9c35557f3ffb06960709c88d144b&language=ko-Kr"
        val jsonRequest: JsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, url, null, { response ->

                val jsonObject: JSONObject = response
                val imageUrl = jsonObject.get("poster_path").toString()

                val defaultImage = R.drawable.android

                Glide.with(this.context)
                    .load(
                        "https://image.tmdb.org/t/p/w500" + imageUrl
                    ) // 불러올 이미지 url
                    .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                    .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                    .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                    .into(view.findViewById<ImageView>(R.id.itemMovieImg))

            }, { error ->
                println(error.printStackTrace())
            }
            )
        queue?.add(jsonRequest)

    }



}