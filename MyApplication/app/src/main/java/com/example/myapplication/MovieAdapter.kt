package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.setPadding
import com.bumptech.glide.Glide


class MovieAdapter(val context: Context, val movieList : ArrayList<Movie>) : BaseAdapter() {
    override fun getCount(): Int {
       return movieList.size
    }

    override fun getItem(position: Int): Any {
        return movieList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {

        val view :View = LayoutInflater.from(context).inflate(R.layout.list_movie_item,null)

        val movieImg = view.findViewById<ImageView>(R.id.movieImg)
        val movieNm = view.findViewById<TextView>(R.id.movieNm)
        val openDt = view.findViewById<TextView>(R.id.openDt)
        val rating = view.findViewById<TextView>(R.id.rating)
        val adult = view.findViewById<TextView>(R.id.adult)


        val movie = movieList[position]


        for(i in 1 .. movie.genreIdsList.size){
            if(i == 6){
                break;
            }
            view.findViewWithTag<TextView>("genreIdsItem" + i).text = movie.genreIdsList.get(i - 1)
            view.findViewWithTag<TextView>("genreIdsItem" + i).setPadding(15) //적
        }


        val defaultImage = R.drawable.android
        val url = movie.movieImg

        try{
            Glide.with(this.context)
                .load(url) // 불러올 이미지 url
                .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                .into(movieImg) // 이미지를 넣을 뷰

        }catch (e : Exception){
            Glide.with(this.context)
                .load(url) // 불러올 이미지 url
                .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                .fallback(defaultImage) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                .into(movieImg) // 이미지를 넣을 뷰
        }



        movieNm.text = movie.movieNm
        openDt.text = movie.openDt
        rating.text = movie.rating + "점"
        if(movie.adult == true){
            adult.text ="청소년 관람불가"
        }else{
            adult.text = "15세 이상 관람가능"
        }

        return view

    }




}