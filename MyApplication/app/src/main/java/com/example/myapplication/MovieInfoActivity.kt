package com.example.myapplication

import android.R
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityMovieInfoBinding
import org.json.JSONArray
import org.json.JSONObject


class MovieInfoActivity : AppCompatActivity() {

    var queue : RequestQueue? = null
    var runtime = ""

    private val binding by lazy {
        ActivityMovieInfoBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        if(queue == null){
            queue = Volley.newRequestQueue(this)
        }

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

        editor.commit()

        val defaultImage = R.drawable.btn_minus
        val url = intent.getStringExtra("movieImg")


        Glide.with(this)
            .load(url) // 불러올 이미지 url
            .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
            .into(binding.movieImageView) // 이미지를 넣을 뷰


        binding.movieInfoNm.text = intent.getStringExtra("movieNm")

        var count : Int = Integer.parseInt(intent.getStringExtra("movieInfoRating")?.get(0).toString())
        var rating =  ""
        var index = 0;
        for(i in 1 ..  count ){
            if(i % 2 == 0) {
                rating += "★"
                index++
                binding.root.findViewWithTag<ImageView>("ratingIcon" + index).setImageResource(R.drawable.btn_star_big_on)
            }
        }

        if(count % 2 == 1){
//            rating += "½"
        }

        for(i in 1 .. (5 -  rating.length)){
            rating += "☆"

            index++
            binding.root.findViewWithTag<ImageView>("ratingIcon" + index).setImageResource(R.drawable.btn_star_big_off)
        }




        binding.movieInfoRating.text = "("+  intent.getStringExtra("movieInfoRating") + ")"
        binding.movieInfoOpenDt.text = intent.getStringExtra("movieInfoOpenDt")

        var movieId = intent.getStringExtra("movieId").toString()

        GetOtherMovieInfo(intent.getStringExtra("movieId").toString(),binding)

        if(intent.getStringExtra("movieInfo").toString().length < 2){
            binding.movieInfo.text = "줄거리 없음"
        }else{
            binding.movieInfo.text = intent.getStringExtra("movieInfo")


            val params: ViewGroup.LayoutParams? =  binding.movieInfo.getLayoutParams()
            if(intent.getStringExtra("movieInfo").toString().length < 300){
                params?.height = intent.getStringExtra("movieInfo").toString().length * 3
            }else{
                params?.height =  (intent.getStringExtra("movieInfo").toString().length * 2.6).toInt()
            }
            binding.movieInfo.setLayoutParams(params);

        }

        binding.movieInfoAdult.text = intent.getStringExtra("adult")

        val rootView = binding.linearLayout3.rootView
        var size = intent.getStringArrayListExtra("genreIdsItem")?.size

        for(i in 0..< size!!){
            if(i == 5){
                break;
            }
             rootView.findViewWithTag<TextView>("InfoGenreIdsItem" + (i + 1) ).text = intent.getStringArrayListExtra("genreIdsItem")?.get(i)
            rootView.findViewWithTag<TextView>("InfoGenreIdsItem" + (i + 1) ).setPadding(15)

        }



        if(prefs.getString("userId",null) != null){

            if(intent.getStringExtra("searchMovieChk") != "search"){

                binding.TicketingBtn.text = "예매하기"
                intent = Intent(this,MovieTicketingActivity::class.java)
                intent.putExtra("movieNm", binding.movieInfoNm.text.toString())
                intent.putExtra("movieOpenDt", binding.movieInfoOpenDt.text.toString())



            }else{
                binding.TicketingBtn.visibility=View.GONE
                val params: ViewGroup.LayoutParams? =  binding.scrollView2.layoutParams
                params?.height = 1270
                binding.scrollView2.setLayoutParams(params);
            }
        }else{
            binding.TicketingBtn.text = "로그인을 해주세요"
            intent = Intent(this,LoginActivity::class.java)
        }


        binding.TicketingBtn.setOnClickListener {

            intent.putExtra("movieId", movieId )
            startActivity(intent)  //여기 코드 수정
        }




    }


    private fun GetOtherMovieInfo(movieId: String, root: ActivityMovieInfoBinding) {

        val url = " https://api.themoviedb.org/3/movie/${movieId}?api_key=b30e9c35557f3ffb06960709c88d144b&language=ko-Kr"
        val jsonRequest: JsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null, { response ->


            val jsonObject : JSONObject = response
            val data = jsonObject.get("production_companies") as JSONArray

            runtime =  jsonObject.get("runtime").toString()


            root.runTime.text =  jsonObject.get("runtime").toString() + "분"
            if( jsonObject.get("tagline").toString().length > 5) {
                root.tagLine.text = "#" + jsonObject.get("tagline").toString()
            }

            val defaultImage = com.example.myapplication.R.drawable.android

            for(i in 0 ..  data.length() - 1){

                if(i == 3){
                    break;
                }

                root.root.findViewWithTag<TextView>("creaditBy" + (i + 1)).text = data.getJSONObject(i).get("name").toString()

                if(data.getJSONObject(i).get("logo_path").toString().length  > 5) {
                    root.root.findViewWithTag<ImageView>("logoImageView" + (i + 1)).visibility = View.VISIBLE

                    Glide.with(this)
                        .load(
                            "https://image.tmdb.org/t/p/w500" + data.getJSONObject(i).get("logo_path")
                        ) // 불러올 이미지 url
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                        .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .into(root.root.findViewWithTag<ImageView>("logoImageView" + (i + 1))) // 이미지를 넣을 뷰

                }

        }},{error ->
            println(error.printStackTrace())
        }
        )
        queue?.add(jsonRequest)
    }

    }